package com.connectfood.core.domain.service;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;

public interface UsersService {
  List<Users> findAll();

  Optional<Users> findByUuid(String uuid);

  Users created(Users user);

  Users updated(Users user);

  void deleteByUuid(String uuid);
}
