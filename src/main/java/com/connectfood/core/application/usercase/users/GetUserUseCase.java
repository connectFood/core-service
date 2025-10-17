package com.connectfood.core.application.usercase.users;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.UserResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetUserUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public UserResponse execute(String uuid) {
    final var user = service.findByUuid(uuid);
    return user.map(mapper::toRsponse)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }
}
