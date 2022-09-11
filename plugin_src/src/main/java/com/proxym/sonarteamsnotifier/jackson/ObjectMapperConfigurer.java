package com.proxym.sonarteamsnotifier.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proxym.sonarteamsnotifier.exceptions.InvalidCastException;

public class ObjectMapperConfigurer {
    private ObjectMapperConfigurer(){}
    public static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return objectMapper.convertValue(o,clazz);
        } catch(ClassCastException e) {
            throw new InvalidCastException("Could not cast to " + clazz.getSimpleName());
        }
    }

}
