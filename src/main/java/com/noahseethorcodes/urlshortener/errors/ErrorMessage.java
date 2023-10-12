package com.noahseethorcodes.urlshortener.errors;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.Objects;

public class ErrorMessage {
    private final LocalDateTime timestamp;
    private Integer status;
    private HttpStatusCode error;
    private String message;
    private String debugMessage;

    private ErrorMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorMessage(HttpStatusCode statusCode, String message, Throwable ex) {
        this();
        this.status = statusCode.value();
        this.error = statusCode;
        this.message = message;
        this.debugMessage = (Objects.equals(message, ex.getLocalizedMessage())) ? "" : ex.getLocalizedMessage();
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public Integer getStatus() {
        return this.status;
    }

    public HttpStatusCode getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDebugMessage() {
        return this.debugMessage;
    }
}
