package com.sales_scout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



public class AuthError extends RuntimeException {

    public ResponseEntity<String> Autherror(){
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
    }

}
