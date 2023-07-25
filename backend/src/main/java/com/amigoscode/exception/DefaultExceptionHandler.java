package com.amigoscode.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiError> handelException(ResourceNotFound e,
                                                    HttpServletRequest request ){

        String message = e.getMessage();
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()

        );
        System.out.println("apiError");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiError> handelException(InsufficientAuthenticationException e,
                                              HttpServletRequest request ){

        String message = e.getMessage();
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()

        );
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handelException(BadCredentialsException e,
                                                    HttpServletRequest request ){

        String message = e.getMessage();
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()

        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handelException(Exception e,
                                                    HttpServletRequest request ){

        String message = e.getMessage();
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()

        );
        System.out.println("apiError");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

