package com.connectfood.core.infrastructure.persistence.specification;

import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

  private UserSpecification() {
  }

  public static Specification<UsersEntity> nameContains(String name) {
    if (name == null || name.isBlank()) {
      return null;
    }

    final String pattern = "%" + name.trim()
        .toLowerCase() + "%";
    return (root, query, cb) -> cb.like(cb.lower(root.get("fullName")), pattern);
  }
}
