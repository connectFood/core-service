package com.connectfood.core.entrypoint.rest.handler;

import java.util.ArrayList;
import java.util.List;

import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.exception.UnauthorizedException;
import com.connectfood.model.ProblemDetails;
import com.connectfood.model.ProblemDetailsErrorsInner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetails> handleNotFoundException(
      final NotFoundException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ProblemDetails> handleConflictException(
      final ConflictException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.CONFLICT, request.getRequestURI());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ProblemDetails> handleUnauthorizedException(
      final UnauthorizedException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetails> handleValidation(
      final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    List<ProblemDetailsErrorsInner> errors = new ArrayList<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.add(new ProblemDetailsErrorsInner().field(error.getField())
            .message(error.getDefaultMessage())));

    return buildApiErrorResponse(
        "Validation failed", HttpStatus.BAD_REQUEST, request.getRequestURI(), errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetails> handleGeneric(
      final Exception exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        "Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
  }

  private ResponseEntity<ProblemDetails> buildApiErrorResponse(
      final String message, final HttpStatus status, final String path) {
    return buildApiErrorResponse(message, status, path, null);
  }

  private ResponseEntity<ProblemDetails> buildApiErrorResponse(
      final String message,
      final HttpStatus status,
      final String path,
      final List<ProblemDetailsErrorsInner> errors) {
    return ResponseEntity.status(status)
        .body(
            new ProblemDetails()
                .type("https://httpstatuses.com/" + status.value())
                .title((status.getReasonPhrase()))
                .status(status.value())
                .detail(message)
                .instance(path)
                .errors(errors));
  }
}
