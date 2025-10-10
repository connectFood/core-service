package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;

public interface UsersRepository {

  List<Users> findAll();

  Optional<Users> findByUuid(String uuid);

  Users save(Users user);

  void deleteByUuid(String uuid);
}
