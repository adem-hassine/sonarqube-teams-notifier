package com.proxym.sonarteamsnotifier.exceptions;

public class InvalidHttpResponseException extends RuntimeException {


  public InvalidHttpResponseException(String errorMessage) {
    super(errorMessage);
  }
}
