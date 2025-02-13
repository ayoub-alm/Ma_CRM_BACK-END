package com.sales_scout.exception;

public class DataAlreadyExistsException extends RuntimeException {
    private String message ;
    public Long code;

    public DataAlreadyExistsException(String message, Long code) {
        super(message);
        this.message = message;
        this.code = code;
    }
    public String getMessage(){
        return message;
    }

    public Long getCode(){
        return code;
    }
}
