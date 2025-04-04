package com.connectify.demo.Security.Handler;

import com.connectify.demo.Exceptions.PostNotFoundException;
import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(PostNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(createNotFoundResponse(e.getLocalMessage(), request), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(createNotFoundResponse(e.getLocalMessage(), request), HttpStatus.BAD_REQUEST);
    }


    private ErrorResponse createNotFoundResponse(String localMessage, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(localMessage);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        return errorResponse;
    }
}
