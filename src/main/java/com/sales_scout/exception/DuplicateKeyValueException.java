package com.sales_scout.exception;

public class DuplicateKeyValueException extends RuntimeException {
  public DuplicateKeyValueException(String message) {
    super(message);
  }

  public DuplicateKeyValueException(String message, Throwable cause) {
    super(message, cause);
  }}
