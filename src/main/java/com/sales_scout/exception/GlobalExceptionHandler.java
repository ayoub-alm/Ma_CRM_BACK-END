package com.sales_scout.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> details = Map.of(
                "resource", ex.getResourceName(),
                "field", ex.getFieldName(),
                "value", ex.getFieldValue()
        );
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, details, ex);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleDataNotFound(DataNotFoundException ex, WebRequest request) {
        Map<String, Object> details = ex.getCode() != null
                ? Map.of("code", ex.getCode())
                : null;
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, details, ex);
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAlreadyExists(DataAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> details = ex.getCode() != null
                ? Map.of("code", ex.getCode())
                : null;
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, details, ex);
    }

    @ExceptionHandler(DuplicateKeyValueException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateKeyValue(DuplicateKeyValueException ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        if (ex.getCause() != null) {
            details.put("cause", ex.getCause().getMessage());
        }
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, details.isEmpty() ? null : details, ex);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, null, ex);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityExists(EntityExistsException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, null, ex);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null, ex);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (first, second) -> second));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", request, details, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> details = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (first, second) -> second
                ));
        return buildResponse(HttpStatus.BAD_REQUEST, "Constraint violation", request, details, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", request, null, ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        Map<String, Object> details = Map.of("supportedMethods", ex.getSupportedHttpMethods());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request, details, ex);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        Map<String, Object> details = Map.of("supportedMediaTypes", ex.getSupportedMediaTypes());
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), request, details, ex);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Data integrity violation", request, null, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request, null, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntime(RuntimeException ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, null, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, null, ex);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                                                           String message,
                                                           WebRequest request,
                                                           Map<String, Object> details,
                                                           Throwable ex) {
        logException(status, message, ex, request);
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(extractPath(request))
                .details(details)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    private void logException(HttpStatus status, String message, Throwable ex, WebRequest request) {
        String path = extractPath(request);
        String logMessage = "Request [{}] failed with status {}: {}";
        if (status.is5xxServerError()) {
            log.error(logMessage, path, status.value(), message, ex);
        } else {
            log.warn(logMessage, path, status.value(), message);
            log.debug("Stack trace:", ex);
        }
    }

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            return servletRequest.getRequest().getRequestURI();
        }
        return null;
    }
}
