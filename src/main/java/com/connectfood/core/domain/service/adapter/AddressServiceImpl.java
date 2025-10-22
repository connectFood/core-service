package com.connectfood.core.domain.service.adapter;

import java.util.List;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.service.AddressService;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository repository;

  @Override
  public List<Address> findAllByUserUuid(String uuid) {
    return repository.findAllByUserUuid(uuid);
  }

  @Override
  public Address save(Address address, String userUuid) {
    return repository.save(address, userUuid);
  }

  @Override
  public void deleteByUserUuid(String userUuid) {
    repository.deleteByUserUuid(userUuid);
  }
}
