package com.cqrs.infrastructure.web;

import com.cqrs.infrastructure.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AggregateNotFoundException.class)
    public ProblemDetail handleAggregateNotFound(AggregateNotFoundException ex) {
        log.warn("Aggregate not found: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Aggregate Not Found");
        problem.setType(URI.create("https://api.cqrs.com/errors/aggregate-not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(CommandValidationException.class)
    public ProblemDetail handleCommandValidation(CommandValidationException ex) {
        log.warn("Command validation failed: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Validation Error");
        problem.setType(URI.create("https://api.cqrs.com/errors/validation-error"));
        problem.setProperty("errors", ex.getErrors());
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(DuplicateCommandException.class)
    public ProblemDetail handleDuplicateCommand(DuplicateCommandException ex) {
        log.warn("Duplicate command: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Duplicate Command");
        problem.setType(URI.create("https://api.cqrs.com/errors/duplicate-command"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(CommandHandlerNotFoundException.class)
    public ProblemDetail handleCommandHandlerNotFound(CommandHandlerNotFoundException ex) {
        log.error("Command handler not found: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Internal Error");
        problem.setType(URI.create("https://api.cqrs.com/errors/internal-error"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(QueryHandlerNotFoundException.class)
    public ProblemDetail handleQueryHandlerNotFound(QueryHandlerNotFoundException ex) {
        log.error("Query handler not found: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Internal Error");
        problem.setType(URI.create("https://api.cqrs.com/errors/internal-error"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Invalid Operation");
        problem.setType(URI.create("https://api.cqrs.com/errors/invalid-operation"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://api.cqrs.com/errors/internal-error"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
