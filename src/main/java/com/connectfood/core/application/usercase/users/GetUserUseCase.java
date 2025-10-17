package com.connectfood.core.application.usercase.users;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.BaseResponseOfUserResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetUserUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public BaseResponseOfUserResponse execute(String uuid) {
    final var user = service.findByUuid(uuid);

    final var response = user.map(mapper::toResponse)
        .orElseThrow(() -> new NotFoundException("User not found"));
    return new BaseResponseOfUserResponse().content(response);
  }
}
