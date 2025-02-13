package com.sales_scout.exception;

public class DataNotFoundException extends RuntimeException {
  private String message ;
  public Long code;

  public DataNotFoundException(String message, Long code) {
    super(message);
    this.message = message ;
    this.code = code ;
  }

  public String getMessage(){
    return message;
  }

  public Long getCode(){
    return code;
  }
}
