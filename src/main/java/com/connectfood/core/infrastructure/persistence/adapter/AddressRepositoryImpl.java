package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.mapper.AddressInfrastructureMapper;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

  private final JpaAddressRepository repository;
  private final JpaUsersRepository usersRepository;
  private final AddressInfrastructureMapper mapper;

  @Override
  public List<Address> findAllByUserUuid(String uuid) {
    final var entities = repository.findAllByUserUuid(UUID.fromString(uuid));

    return entities.stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Address save(Address address, String userUuid) {
    final var userEntity = usersRepository.findByUuid(UUID.fromString(userUuid))
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var entity = mapper.toEntity(address, userEntity);
    return mapper.toDomain(repository.save(entity));
  }

  @Override
  public void deleteByUserUuid(String userUuid) {
    final var entities = repository.findAllByUserUuid(UUID.fromString(userUuid));

    repository.deleteAll(entities);
  }
}
