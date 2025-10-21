package com.connectfood.core.infrastructure.security;

import java.io.IOException;

import com.connectfood.core.domain.factory.ProblemDetailsFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final ProblemDetailsFactory factory;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

    final var body = factory.build(HttpStatus.FORBIDDEN,
        accessDeniedException.getMessage() != null ? accessDeniedException.getMessage() : "Forbidden",
        request.getRequestURI()
    );

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    mapper.writeValue(response.getOutputStream(), body);
  }
}
