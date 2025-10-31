package com.connectfood.core.domain.enums;

import java.util.Arrays;

import com.connectfood.core.domain.exception.BadRequestException;

public enum UsersRole {

  CUSTOMER,
  OWNER;

  public static void validatedUserRole(String value) {
    Arrays.stream(values())
        .filter(role -> role.name()
            .equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new BadRequestException("Invalid user role"));
  }
}
