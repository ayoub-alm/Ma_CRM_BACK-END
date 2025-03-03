package com.sales_scout.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {
  private String message ;
  public Long code;

  public DataNotFoundException(String message, Long code) {
    super(message);
    this.message = message ;
    this.code = code ;
  }

}
