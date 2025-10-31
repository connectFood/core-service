package com.connectfood.core.application.usercase.users;

import java.util.List;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.enums.UsersRole;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.BaseResponseOfUserResponse;
import com.connectfood.model.UserUpdateRequest;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public BaseResponseOfUserResponse execute(String uuid, UserUpdateRequest request) {
    final var user = service.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    validatedUsersRole(request.getRoles());
    validatedEmail(request.getEmail(), user.getEmail());

    final var userUpdate = mapper.update(request, user);
    final var result = service.updated(uuid, userUpdate);

    final var response = mapper.toResponse(result);
    return new BaseResponseOfUserResponse().content(response);
  }

  private void validatedEmail(String newEmail, String currentEmail) {
    if (newEmail != null) {
      if (!newEmail.equals(currentEmail)) {
        service.validatedEmail(newEmail);
      }
    }
  }

  private void validatedUsersRole(List<String> roles) {
    if (!roles.isEmpty()) {
      roles.forEach(UsersRole::validatedUserRole);
    }
  }
}
