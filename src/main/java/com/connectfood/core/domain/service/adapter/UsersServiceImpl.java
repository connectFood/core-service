package com.connectfood.core.domain.service.adapter;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.UserRole;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

  private final UsersRepository repository;

  @Override
  public PageModel<List<Users>> findAll(String name, UserRole role, Integer page, Integer size) {
    return repository.findAll(name, role, page, size);
  }

  @Override
  public Optional<Users> findByUuid(String uuid) {
    return repository.findByUuid(uuid);
  }

  @Override
  public Users created(Users user) {
    return repository.save(user);
  }

  @Override
  public Users updated(Users user) {
    return repository.save(user);
  }

  @Override
  public void deleteByUuid(String uuid) {
    repository.deleteByUuid(uuid);
  }
}
