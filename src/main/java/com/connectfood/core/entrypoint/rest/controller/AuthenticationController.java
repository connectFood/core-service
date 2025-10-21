package com.connectfood.core.entrypoint.rest.controller;

import com.connectfood.api.AuthenticationApi;
import com.connectfood.core.application.usercase.authentication.AuthenticationUseCase;
import com.connectfood.model.BaseResponseOfJwtTokenResponse;
import com.connectfood.model.LoginValidationRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

  private final AuthenticationUseCase authenticationUseCase;

  @Override
  public ResponseEntity<BaseResponseOfJwtTokenResponse> login(LoginValidationRequest request) {
    final var response = authenticationUseCase.execute(request);
    return ResponseEntity.ok(response);
  }
}
