package com.connectfood.core.application.usercase.users;

import com.connectfood.core.application.mapper.AddressMapper;
import com.connectfood.core.application.mapper.UsersMapper;
import com.connectfood.core.domain.enums.UsersRole;
import com.connectfood.core.domain.service.AddressService;
import com.connectfood.core.domain.service.UsersService;
import com.connectfood.model.BaseResponseOfUserResponse;
import com.connectfood.model.UserCreateRequest;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UsersService service;
  private final AddressService addressService;
  private final UsersMapper mapper;
  private final AddressMapper addressMapper;

  @Transactional
  public BaseResponseOfUserResponse execute(UserCreateRequest request) {
    validatedUsersRole(request.getRoles());

    final var user = service.created(mapper.create(request));

    final var addresses = request.getAddresses()
        .stream()
        .map(address -> addressService.save(addressMapper.create(address), user.getUuid()))
        .toList();

    final var addressesResponse = addressMapper.toResponses(addresses);

    final var response = mapper.toResponse(user, addressesResponse);
    return new BaseResponseOfUserResponse().content(response);
  }

  private void validatedUsersRole(List<String> roles) {
    roles.forEach(UsersRole::validatedUserRole);
  }
}
