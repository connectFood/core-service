package com.connectfood.core.application.mapper;

import java.time.ZoneId;
import java.util.List;

import com.connectfood.core.domain.model.Users;
import com.connectfood.model.UserCreateRequest;
import com.connectfood.model.UserResponse;
import com.connectfood.model.UserUpdateRequest;

import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

  public UserResponse toRsponse(Users user) {

    UserResponse response = new UserResponse();
    response.setUuid(user.getUuid());
    response.setFullName(user.getFullName());
    response.setEmail(user.getEmail());
    response.setLogin(user.getLogin());
    response.setRoles(user.getRoles());
    response.setCreatedAt(user.getCreatedAt()
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime());
    response.setLastUpdateAt(user.getUpdatedAt()
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime());
    return response;
  }

  public List<UserResponse> toResponses(List<Users> users) {
    return users.stream()
        .map(this::toRsponse)
        .toList();
  }

  public Users create(UserCreateRequest request) {
    return Users.builder()
        .fullName(request.getFullName())
        .email(request.getEmail())
        .login(request.getLogin())
        .password(request.getPassword())
        .roles(request.getRoles())
        .build();
  }

  public Users update(UserUpdateRequest request, Users user) {
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setLogin(request.getLogin());
    user.setRoles(request.getRoles());
    return user;
  }
}
