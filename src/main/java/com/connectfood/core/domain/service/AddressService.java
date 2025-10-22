package com.connectfood.core.domain.service;

import java.util.List;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;

public interface AddressService {
  List<Address> findAllByUserUuid(String uuid);

  Address save(Address address, String userUuid);

  void deleteByUserUuid(String userUuid);
}
