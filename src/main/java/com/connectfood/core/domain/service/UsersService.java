package com.connectfood.core.domain.service;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.model.UserRole;

public interface UsersService {
  PageModel<List<Users>> findAll(String name, UserRole role, Integer page, Integer size);

  Optional<Users> findByUuid(String uuid);

  Users created(Users user);

  Users updated(Users user);

  void deleteByUuid(String uuid);
}
