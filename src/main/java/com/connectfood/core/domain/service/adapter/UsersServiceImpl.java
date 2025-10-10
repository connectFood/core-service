package com.connectfood.core.domain.service.adapter;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.service.UsersService;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

  private final UsersRepository repository;

  @Override
  public List<Users> findAll() {
    return repository.findAll();
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
