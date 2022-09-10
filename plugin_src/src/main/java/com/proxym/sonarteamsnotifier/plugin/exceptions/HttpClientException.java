package com.proxym.sonarteamsnotifier.plugin.exceptions;

public class HttpClientException extends RuntimeException{
  public HttpClientException(String message){
    super(message);
  }
}
