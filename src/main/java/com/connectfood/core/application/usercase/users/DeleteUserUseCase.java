package com.connectfood.core.application.usercase.users;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.service.UsersService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase {

  private final UsersService service;

  public void execute(String uuid) {
    final var user = service.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    service.deleteByUuid(user.getUuid());
  }
}
