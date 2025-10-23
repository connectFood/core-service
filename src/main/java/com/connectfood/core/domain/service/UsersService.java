package com.connectfood.core.domain.service;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;

public interface UsersService {
  PageModel<List<Users>> findAll(String name, Integer page, Integer size);

  Optional<Users> findByUuid(String uuid);

  Users created(Users user);

  Users updated(String uuid, Users user);

  void changedPassword(String uuid, Users user);

  void deleteByUuid(String uuid);

  Optional<Users> findByLoginOrEmail(String login, String email);

  void validatedEmail(String email);
}
