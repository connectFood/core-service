package com.connectfood.core.domain.service.adapter;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.core.domain.utils.PasswordUtils;
import com.connectfood.model.UserRole;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

  private final UsersRepository repository;

  @Override
  public PageModel<List<Users>> findAll(String name, Integer page, Integer size) {
    return repository.findAll(name, page, size);
  }

  @Override
  public Optional<Users> findByUuid(String uuid) {
    return repository.findByUuid(uuid);
  }

  @Override
  public Users created(Users user) {
    validatedEmail(user.getEmail());

    final var password = PasswordUtils.encode(user.getPassword());
    user.setPassword(password);
    return repository.save(user);
  }

  @Override
  public Users updated(Users user) {
    validatedEmail(user.getEmail());

    return repository.save(user);
  }

  @Override
  public void changedPassword(Users user) {
    repository.save(user);
  }

  @Override
  public void deleteByUuid(String uuid) {
    repository.deleteByUuid(uuid);
  }

  @Override
  public Optional<Users> findByLoginOrEmail(String login, String email) {
    return repository.findByLoginOrEmail(login, email);
  }

  private void validatedEmail(String email) {
    final var existsEmail = repository.existsByEmail(email);
    if (existsEmail) {
      throw new ConflictException("Email already registered in the system");
    }
  }
}
