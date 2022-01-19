package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exception.DatabaseException;
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
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = buildError(e.getMessage(), req.getRequestURI(), status, "Resource not found");
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(ResourceNotFoundException e, HttpServletRequest req){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = buildError(e.getMessage(), req.getRequestURI(), status, "Database Exception.");
        return ResponseEntity.status(status).body(error);
    }

    private StandardError buildError(String msg, String path, HttpStatus status, String errorStr){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setMessage(msg);
        error.setPath(path);
        error.setStatus(status.value());
        error.setError(errorStr);
        return error;
    }

}
