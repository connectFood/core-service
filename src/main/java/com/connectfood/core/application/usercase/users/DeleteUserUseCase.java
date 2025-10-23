package com.connectfood.core.application.usercase.users;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.service.AddressService;
import com.connectfood.core.domain.service.UsersService;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase {

  private final UsersService service;
  private final AddressService addressService;

  @Transactional
  public void execute(String uuid) {
    final var user = service.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    addressService.deleteByUserUuid(user.getUuid());
    service.deleteByUuid(user.getUuid());
  }
}
