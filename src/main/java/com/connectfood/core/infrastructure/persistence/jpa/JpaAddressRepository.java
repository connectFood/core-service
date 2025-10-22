package com.connectfood.core.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaAddressRepository extends JpaRepository<AddressEntity, Long>,
    JpaSpecificationExecutor<AddressEntity> {

  List<AddressEntity> findAllByUserUuid(UUID uuid);

  Optional<AddressEntity> findByUuid(UUID uuid);

  void deleteByUserUuid(UUID userUuid);
}
