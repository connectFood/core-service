package com.connectfood.core.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaUsersRepository extends JpaRepository<UsersEntity, Long>, JpaSpecificationExecutor<UsersEntity> {

  Optional<UsersEntity> findByUuid(UUID uuid);

  Optional<UsersEntity> findByLoginOrEmail(String login, String email);

  void deleteByUuid(UUID uuid);

  boolean existsByEmail(String email);
}
