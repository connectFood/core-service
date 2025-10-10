package com.connectfood.core.domain.model;

import java.util.List;

import com.connectfood.core.domain.model.commons.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseModel {

  private String fullName;
  private String email;
  private String login;
  private String password;
  private List<String> roles;
}
