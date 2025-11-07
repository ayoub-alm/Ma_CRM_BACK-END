package com.sales_scout.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.entity.AuditLog;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLoggingAspect {

    private static final int MAX_CONTENT_LENGTH = 4000;

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object auditEndpointInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = getCurrentHttpRequest();
        UserEntity currentUser = SecurityUtils.getCurrentUser();
        UserEntity actingUserReference = toReference(currentUser);

        AuditLog.AuditLogBuilder auditBuilder = AuditLog.builder()
                .httpMethod(request != null ? request.getMethod() : "UNKNOWN")
                .endpoint(request != null ? request.getRequestURI() : joinPoint.getSignature().toShortString())
                .queryParams(extractQueryParams(request))
                .requestBody(extractRequestBody(joinPoint.getArgs()))
                .clientIp(extractClientIp(request))
                .username(resolveUsername(currentUser))
                .actingUser(actingUserReference);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            populateResponseDetails(auditBuilder, result);
            auditBuilder.durationMs(duration);
            persistAuditLog(auditBuilder.build());
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - startTime;
            auditBuilder
                    .durationMs(duration)
                    .responseStatus(resolveStatusFromException(ex))
                    .errorMessage(truncate(ex.getMessage()));
            persistAuditLog(auditBuilder.build());
            throw ex;
        }
    }

    private void populateResponseDetails(AuditLog.AuditLogBuilder builder, Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            builder.responseStatus(responseEntity.getStatusCode().value());
            builder.responseBody(truncate(safeSerialize(responseEntity.getBody())));
        } else if (result == null) {
            builder.responseStatus(HttpStatus.NO_CONTENT.value());
            builder.responseBody(null);
        } else {
            builder.responseStatus(HttpStatus.OK.value());
            builder.responseBody(truncate(safeSerialize(result)));
        }
    }

    private String extractQueryParams(HttpServletRequest request) {
        if (request == null || request.getParameterMap().isEmpty()) {
            return null;
        }
        Map<String, Object> flattened = new LinkedHashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values == null) {
                flattened.put(key, null);
            } else if (values.length == 1) {
                flattened.put(key, values[0]);
            } else {
                flattened.put(key, Arrays.asList(values));
            }
        });
        return truncate(safeSerialize(flattened));
    }

    private String extractRequestBody(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        List<Object> sanitizedArgs = Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> !(arg instanceof HttpServletRequest))
                .filter(arg -> !(arg instanceof HttpServletResponse))
                .filter(arg -> !(arg instanceof MultipartFile))
                .filter(arg -> !(arg instanceof MultipartFile[]))
                .filter(arg -> !(arg instanceof BindingResult))
                .filter(arg -> !(arg instanceof Principal))
                .map(this::sanitizeArgument)
                .filter(Objects::nonNull)
                .toList();

        if (sanitizedArgs.isEmpty()) {
            return null;
        }

        Object bodyContent = sanitizedArgs.size() == 1 ? sanitizedArgs.get(0) : sanitizedArgs;
        return truncate(safeSerialize(bodyContent));
    }

    private Object sanitizeArgument(Object arg) {
        if (arg == null) {
            return null;
        }

        if (isSimpleValue(arg)) {
            return arg;
        }

        if (arg instanceof Collection<?> collection) {
            return collection.stream()
                    .map(this::sanitizeArgument)
                    .filter(Objects::nonNull)
                    .toList();
        }

        if (arg.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(arg);
            List<Object> values = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                values.add(sanitizeArgument(java.lang.reflect.Array.get(arg, i)));
            }
            return values;
        }

        if (arg instanceof Map<?, ?> map) {
            Map<Object, Object> sanitized = new LinkedHashMap<>();
            map.forEach((key, value) -> sanitized.put(key, sanitizeArgument(value)));
            return sanitized;
        }

        if (arg instanceof Optional<?> optional) {
            return optional.map(this::sanitizeArgument).orElse(null);
        }

        // Attempt JSON serialization; fall back to string representation.
        String serialized = safeSerialize(arg);
        if (serialized != null && serialized.startsWith("\"") && serialized.endsWith("\"")) {
            return serialized.substring(1, serialized.length() - 1);
        }
        return serialized != null ? serialized : String.valueOf(arg);
    }

    private boolean isSimpleValue(Object arg) {
        return arg instanceof String
                || arg instanceof Number
                || arg instanceof Boolean
                || arg instanceof Character
                || arg instanceof Enum<?>;
    }

    private String extractClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    private String resolveUsername(UserEntity user) {
        if (user != null && user.getEmail() != null) {
            return user.getEmail();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String principalName) {
                return principalName;
            }
            return authentication.getName();
        }
        return "anonymous";
    }

    private int resolveStatusFromException(Throwable ex) {
        if (ex instanceof ResponseStatusException responseStatusException) {
            return responseStatusException.getStatusCode().value();
        }
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.code().value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private String safeSerialize(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.debug("Failed to serialize value of type {} for audit log: {}", value.getClass().getName(), e.getMessage());
            return String.valueOf(value);
        }
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        if (value.length() <= MAX_CONTENT_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_CONTENT_LENGTH);
    }

    private void persistAuditLog(AuditLog auditLog) {
        try {
            auditLogService.record(auditLog);
        } catch (Exception persistenceException) {
            log.warn("Failed to persist audit log for endpoint {}: {}", auditLog.getEndpoint(), persistenceException.getMessage());
        }
    }

    private UserEntity toReference(UserEntity user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        UserEntity reference = new UserEntity();
        reference.setId(user.getId());
        return reference;
    }
}
