package com.proxym.sonarteamsnotifier.exceptions;

public class InitialMetricsFileNotFound extends RuntimeException{
    public InitialMetricsFileNotFound(String message){
        super(message);
    }
}
