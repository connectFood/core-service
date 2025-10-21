package com.connectfood.core.infrastructure.security;

import java.util.stream.Collectors;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

  private final JpaUsersRepository usersRepository;

  public UserDetails loadUserByUsername(String username) {
    final var user = usersRepository.findByLoginOrEmail(username, username)
        .orElseThrow(() -> new NotFoundException(
            "User not found"));

    final var authorities =
        user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());

    return User.withUsername(user.getLogin())
        .password(user.getPassword())
        .authorities(authorities)
        .accountLocked(false)
        .disabled(false)
        .build();
  }
}
