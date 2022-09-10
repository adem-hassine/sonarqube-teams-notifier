package com.proxym.sonarteamsnotifier.plugin.exceptions;

public class InvalidHttpResponseException extends RuntimeException {

  /**
   * Constructor.
   *
   * @param errorMessage The error message of the exception.
   */
  public InvalidHttpResponseException(String errorMessage) {
    super(errorMessage);
  }
}
