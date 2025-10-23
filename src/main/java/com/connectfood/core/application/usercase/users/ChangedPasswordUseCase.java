package com.connectfood.core.application.usercase.users;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.exception.UnauthorizedException;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.core.domain.utils.PasswordUtils;
import com.connectfood.model.ChangePasswordRequest;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangedPasswordUseCase {

  private final UsersService service;

  public void execute(String uuid, ChangePasswordRequest request) {
    final var user = service.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var validPassword = PasswordUtils.matches(request.getCurrentPassword(), user.getPassword());

    if (!validPassword) {
      throw new UnauthorizedException("Invalid password");
    }

    final var password = PasswordUtils.encode(request.getNewPassword());
    user.setPassword(password);
    service.changedPassword(uuid, user);
  }
}
