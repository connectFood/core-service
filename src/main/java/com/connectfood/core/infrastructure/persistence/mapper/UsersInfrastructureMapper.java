package com.connectfood.core.infrastructure.persistence.mapper;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.stereotype.Component;

@Component
public class UsersInfrastructureMapper {

  public Users toDomain(UsersEntity entity) {
    return Users.builder()
        .id(entity.getId())
        .uuid(entity.getUuid()
            .toString())
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

  public UsersEntity toEntity(Users user) {
    return UsersEntity.builder()
        .fullName(user.getFullName())
        .email(user.getEmail())
        .login(user.getLogin())
        .password(user.getPassword())
        .roles(user.getRoles())
        .build();
  }

  public UsersEntity toEntity(UsersEntity entity, Users user) {
    if (user.getFullName() != null) {
      entity.setFullName(user.getFullName());
    }
    if (user.getEmail() != null) {
      entity.setEmail(user.getEmail());
    }
    if (user.getLogin() != null) {
      entity.setLogin(user.getLogin());
    }
    if (user.getRoles() != null) {
      entity.setRoles(user.getRoles());
    }
    return entity;
  }

  public UsersEntity toEntity(UsersEntity entity, String password) {
    entity.setPassword(password);
    return entity;
  }
}
