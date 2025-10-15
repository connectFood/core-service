package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.mapper.UsersInfrastructureMapper;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

  private final JpaUsersRepository repository;
  private final UsersInfrastructureMapper mapper;

  @Override
  public List<Users> findAll() {
    final var entities = repository.findAll();

    return entities.stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Users> findByUuid(String uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public Users save(Users user) {
    final var entity = repository.save(mapper.toEntity(user));
    return mapper.toDomain(entity);
  }

  @Override
  public void deleteByUuid(String uuid) {
    repository.deleteByUuid(uuid);
  }
}
