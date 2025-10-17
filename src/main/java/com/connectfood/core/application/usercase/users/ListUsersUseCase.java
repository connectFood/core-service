package com.connectfood.core.application.usercase.users;

import java.util.List;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.UserResponse;
import com.connectfood.model.UserRole;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ListUsersUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public List<UserResponse> execute(String name, UserRole role, Integer page, Integer size) {
    final var users = service.findAll();
    return mapper.toResponses(users);
  }
}
