package com.connectfood.core.domain.factory;

import java.util.List;

import com.connectfood.model.ProblemDetails;
import com.connectfood.model.ProblemDetailsErrorsInner;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ProblemDetailsFactory {

  public ProblemDetails build(HttpStatus status, String message, String path) {
    return build(status, message, path, null);
  }

  public ProblemDetails build(HttpStatus status, String message, String path,
      List<ProblemDetailsErrorsInner> errors) {
    return new ProblemDetails()
        .type("https://httpstatuses.com/" + status.value())
        .title(status.getReasonPhrase())
        .status(status.value())
        .detail(message)
        .instance(path)
        .errors(errors);
  }
}
