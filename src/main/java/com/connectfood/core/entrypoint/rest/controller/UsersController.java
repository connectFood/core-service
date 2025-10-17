package com.connectfood.core.entrypoint.rest.controller;

import com.connectfood.api.UsersApi;
import com.connectfood.core.application.usercase.users.CreateUserUseCase;
import com.connectfood.core.application.usercase.users.GetUserUseCase;
import com.connectfood.core.application.usercase.users.ListUsersUseCase;
import com.connectfood.core.application.usercase.users.UpdateUserUseCase;
import com.connectfood.model.BaseResponseOfUserResponse;
import com.connectfood.model.ChangePasswordRequest;
import com.connectfood.model.PageResponseOfUserResponse;
import com.connectfood.model.UserCreateRequest;
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
  private final ListUsersUseCase listUsersUseCase;
  private final GetUserUseCase getUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;

  @Override
  public ResponseEntity<BaseResponseOfUserResponse> createUser(@Valid UserCreateRequest request) {
    final var result = createUserUseCase.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponseOfUserResponse());
  }

  @Override
  public ResponseEntity<PageResponseOfUserResponse> listUsers(String name, UserRole role, Integer page, Integer size) {
    final var result = listUsersUseCase.execute(name, role, page, size);
    return ResponseEntity.ok(new PageResponseOfUserResponse());
  }

  @Override
  public ResponseEntity<BaseResponseOfUserResponse> getUserByUuid(String uuid) {
    final var result = getUserUseCase.execute(uuid);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<BaseResponseOfUserResponse> updateUser(String uuid, @Valid UserUpdateRequest request) {
    final var result = updateUserUseCase.execute(uuid, request);
    return ResponseEntity.ok(new BaseResponseOfUserResponse());
  }

  @Override
  public ResponseEntity<Void> deleteUser(String uuid) {
    return UsersApi.super.deleteUser(uuid);
  }

  @Override
  public ResponseEntity<Void> changePassword(String uuid, @Valid ChangePasswordRequest body) {
    return ResponseEntity.noContent()
        .build();
  }
}
