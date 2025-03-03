package com.sales_scout.controller;

import com.sales_scout.Auth.JwtHelper;
import com.sales_scout.config.AuthConfig;
import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.request.create.JwtRequest;
import com.sales_scout.dto.response.JwtResponse;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.UserAlreadyExistsException;
import com.sales_scout.repository.RoleRepository;
import com.sales_scout.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> createUser(@RequestBody UserRequestDto userRequestDto) throws UserAlreadyExistsException {
        try {
            // Create a new user
            userRequestDto.setRole(roleRepository.findByRoleAndDeletedAtIsNull("User"));

            UserResponseDto userResponseDto = userService.createUser(userRequestDto);

            // Load user details to generate JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(userResponseDto.getEmail());
            String token = jwtHelper.generateToken(userDetails);

            // Return the JWT token in the response
            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(token)
                    .userResponseDto(userDetails)
                    .build();

            return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            throw new UserAlreadyExistsException(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) throws DataNotFoundException {
        try {
            // Authenticate the user
            authenticateUser(jwtRequest.getEmail(), jwtRequest.getPassword());

            // Load user details to generate JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
            String token = jwtHelper.generateToken(userDetails);

            // Build the user response
            UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
            System.out.println("currentUser: {}"+ user.getUsername());


            // Return the JWT and user details in the response
            // #TODO fix response content should contain clean data
             UserResponseDto userResponseDto = userService.findByEmail(user.getUsername());
            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(token)
                    .user(userResponseDto)
                    .build();

            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            logger.error("Authentication failed: {}", ex.getMessage());
            JwtResponse jwtResponse = JwtResponse.builder()
//                    .token(token)
//                    .userResponseDto(user)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(jwtResponse);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    private void authenticateUser(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("Authentication successful for user: {}", email);
        } catch (BadCredentialsException ex) {
            logger.error("Authentication failed for user: {}", email);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
