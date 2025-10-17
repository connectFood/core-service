package com.connectfood.core.application.usercase.users;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.UserCreateRequest;
import com.connectfood.model.UserResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public UserResponse execute(UserCreateRequest request) {
    final var user = service.created(mapper.create(request));
    return mapper.toResponse(user);
  }
}
