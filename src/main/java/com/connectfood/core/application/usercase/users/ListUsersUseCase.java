package com.connectfood.core.application.usercase.users;

import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.PageResponseOfUserResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ListUsersUseCase {

  private final UsersService service;
  private final UsersMapper mapper;

  public PageResponseOfUserResponse execute(String name, Integer page, Integer size) {
    final var users = service.findAll(name, page, size);

    final var response = mapper.toResponses(users.content());

    return new PageResponseOfUserResponse().content(response)
        .totalElements(users.totalElements())
        .page(page)
        .size(size);
  }
}
