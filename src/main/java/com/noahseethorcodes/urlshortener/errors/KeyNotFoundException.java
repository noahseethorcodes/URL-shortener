package com.noahseethorcodes.urlshortener.errors;

public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException(String url_key) {
        super(String.format("Provided url_key {%s} not found. " +
                "Please check you have input the correct key.", url_key));
    }
}
