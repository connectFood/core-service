package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.mapper.UsersInfrastructureMapper;
import com.connectfood.core.infrastructure.persistence.specification.UserSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

  private final JpaUsersRepository repository;
  private final UsersInfrastructureMapper mapper;

  @Override
  public PageModel<List<Users>> findAll(String name, Integer page, Integer size) {
    final var spec = Specification.allOf(UserSpecification.nameContains(name));
    final var pageable = PageRequest.of(page, size);

    final var entities = repository.findAll(spec, pageable);

    final var result = entities.stream()
        .map(mapper::toDomain)
        .toList();

    return new PageModel<>(result, entities.getTotalElements());
  }

  @Override
  public Optional<Users> findByUuid(String uuid) {
    final var entity = repository.findByUuid(UUID.fromString(uuid));

    return entity.map(mapper::toDomain);
  }

  @Override
  public Users save(Users user) {
    final var entity = repository.save(mapper.toEntity(user));
    return mapper.toDomain(entity);
  }

  @Override
  public Users save(String uuid, Users user) {
    final var entity = repository.findByUuid(UUID.fromString(uuid))
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var entityUpdated = repository.save(mapper.toEntity(entity, user));
    return mapper.toDomain(entityUpdated);
  }

  @Override
  public Users changedPassword(String uuid, Users user) {
    final var entity = repository.findByUuid(UUID.fromString(uuid))
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var entityUpdated = repository.save(mapper.toEntity(entity, user.getPassword()));
    return mapper.toDomain(entityUpdated);
  }

  @Override
  public void deleteByUuid(String uuid) {
    repository.deleteByUuid(UUID.fromString(uuid));
  }

  @Override
  public Optional<Users> findByLoginOrEmail(String login, String email) {
    return repository.findByLoginOrEmail(login, email)
        .map(mapper::toDomain);
  }

  @Override
  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }
}
