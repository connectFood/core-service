package com.connectfood.core.application.usercase.authentication;

import java.util.HashMap;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.core.infrastructure.security.JwtService;
import com.connectfood.model.BaseResponseOfJwtTokenResponse;
import com.connectfood.model.JwtTokenResponse;
import com.connectfood.model.LoginValidationRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationUseCase {

  private final AuthenticationManager authenticationManager;
  private final UsersService service;
  private final JwtService jwtService;

  public BaseResponseOfJwtTokenResponse execute(LoginValidationRequest request) {

    final var username = request.getLogin();
    final var password = request.getPassword();

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    final var user = service.findByLoginOrEmail(username, username)
        .orElseThrow(() -> new BadRequestException("Invalid credentials"));

    final var claims = new HashMap<String, Object>();
    claims.put("fullName", user.getFullName());
    claims.put("email", user.getEmail());
    claims.put("uuid", user.getUuid());
    claims.put("roles", user.getRoles());

    final var token = jwtService.generate(user.getLogin(), claims);

    final var payload = new JwtTokenResponse()
        .accessToken(token)
        .tokenType("Bearer")
        .expiresIn(jwtService.getExpiration());

    return new BaseResponseOfJwtTokenResponse()
        .success(true)
        .content(payload);
  }
}
