package com.noahseethorcodes.urlshortener.errors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public GlobalExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {

        final String message = "Invalid URL provided";
        ErrorMessage body = new ErrorMessage(status, message, ex);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @ResponseBody
    @ExceptionHandler(KeyNotFoundException.class)
    ResponseEntity<ErrorMessage> KeyNotFoundHandler(KeyNotFoundException ex) {
        ErrorMessage error_msg = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return new ResponseEntity<ErrorMessage>(error_msg, error_msg.getError());
    }

    @ResponseBody
    @ExceptionHandler(TargetURLNotFound.class)
    ResponseEntity<ErrorMessage> TargetURLNotFoundHandler(TargetURLNotFound ex) {
        ErrorMessage error_msg = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return new ResponseEntity<ErrorMessage>(error_msg, error_msg.getError());
    }
}
