package com.connectfood.core.application.usercase.users;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.UserResponse;
import com.connectfood.model.UserUpdateRequest;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public UserResponse execute(String uuid, UserUpdateRequest request) {
    final var user = service.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));
    final var result = service.updated(mapper.update(request, user));

    return mapper.toResponse(result);
  }
}
