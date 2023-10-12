package com.noahseethorcodes.urlshortener.errors;

public class TargetURLNotFound extends RuntimeException {
    public TargetURLNotFound(String target_url, String url_key) {
        super(String.format("The target URL '%s' behind url_key {%s} could not be found. " +
                "Please check if the website on the URL exists.", target_url, url_key));
    }
}
