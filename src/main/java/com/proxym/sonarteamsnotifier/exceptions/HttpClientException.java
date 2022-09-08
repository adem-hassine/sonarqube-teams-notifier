package com.proxym.sonarteamsnotifier.exceptions;

public class HttpClientException extends RuntimeException{
  public HttpClientException(String message){
    super(message);
  }
}
