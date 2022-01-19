package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest req){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setMessage(e.getMessage());
        error.setPath(req.getRequestURI());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Resource not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
