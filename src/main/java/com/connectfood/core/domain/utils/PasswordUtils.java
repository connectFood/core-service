package com.connectfood.core.domain.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordUtils {

  private static final PasswordEncoder enconder = new BCryptPasswordEncoder();

  public static String encode(final String password) {
    return enconder.encode(password);
  }

  public static boolean matches(final String password, final String encodedPassword) {
    return enconder.matches(password, encodedPassword);
  }
}
