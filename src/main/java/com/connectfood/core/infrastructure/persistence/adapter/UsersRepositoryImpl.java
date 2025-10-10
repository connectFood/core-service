package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

  private final JpaUsersRepository repository;

  @Override
  public List<Users> findAll() {
    final var entities = repository.findAll();

    return entities.stream()
        .map(this::toDomain)
        .toList();
  }

  @Override
  public Optional<Users> findByUuid(String uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(this::toDomain);
  }

  @Override
  public Users save(Users user) {
    final var entity = repository.save(toEntity(user));
    return toDomain(entity);
  }

  @Override
  public void deleteByUuid(String uuid) {
    repository.deleteByUuid(uuid);
  }

  private Users toDomain(UsersEntity entity) {
    return Users.builder()
        .id(entity.getId())
        .uuid(entity.getUuid())
        .fullName(entity.getFullName())
        .email(entity.getEmail())
        .login(entity.getLogin())
        .password(entity.getPassword())
        .roles(entity.getRoles())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .version(entity.getVersion())
        .build();
  }

  private UsersEntity toEntity(Users user) {
    return UsersEntity.builder()
        .fullName(user.getFullName())
        .email(user.getEmail())
        .login(user.getLogin())
        .password(user.getPassword())
        .roles(user.getRoles())
        .build();
  }
}
