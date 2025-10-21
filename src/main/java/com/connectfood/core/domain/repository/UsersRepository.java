package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.model.UserRole;

public interface UsersRepository {

  PageModel<List<Users>> findAll(String name, UserRole role, Integer page, Integer size);

  Optional<Users> findByUuid(String uuid);

  Users save(Users user);

  void deleteByUuid(String uuid);

  Optional<Users> findByLoginOrEmail(String login, String email);
}
