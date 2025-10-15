package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;

import com.connectfood.api.UsersApi;
import com.connectfood.core.application.usercase.users.CreateUserUseCase;
import com.connectfood.model.ChangePasswordRequest;
import com.connectfood.model.UserCreateRequest;
import com.connectfood.model.UserResponse;
import com.connectfood.model.UserRole;
import com.connectfood.model.UserUpdateRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

  private final CreateUserUseCase createUserUseCase;

  @Override
  public ResponseEntity<UserResponse> createUser(@Valid UserCreateRequest request) {

    final var result = createUserUseCase.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  @Override
  public ResponseEntity<List<UserResponse>> listUsers(String name, UserRole role) {
    return ResponseEntity.ok(List.of());
  }

  @Override
  public ResponseEntity<UserResponse> getUserByUuid(String uuid) {
    return ResponseEntity.ok(new UserResponse());
  }

  @Override
  public ResponseEntity<UserResponse> updateUser(String uuid, @Valid UserUpdateRequest body) {
    return ResponseEntity.ok(new UserResponse());
  }

  @Override
  public ResponseEntity<Void> changePassword(String uuid, @Valid ChangePasswordRequest body) {
    return ResponseEntity.noContent()
        .build();
  }
}
