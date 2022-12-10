package com.proxym.sonarteamsnotifier.exceptions;

public class MetricNotFoundException extends RuntimeException{
    public MetricNotFoundException(){
        super("Metric Not found Exception");
    }
}
