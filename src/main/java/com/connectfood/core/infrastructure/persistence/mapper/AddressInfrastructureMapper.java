package com.connectfood.core.infrastructure.persistence.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.stereotype.Component;

@Component
public class AddressInfrastructureMapper {

  public Address toDomain(AddressEntity entity) {
    return Address.builder()
        .id(entity.getId())
        .uuid(entity.getUuid()
            .toString())
        .street(entity.getStreet())
        .number(entity.getNumber())
        .complement(entity.getComplement())
        .neighborhood(entity.getNeighborhood())
        .city(entity.getCity())
        .state(entity.getState())
        .zipCode(entity.getZipCode())
        .country(entity.getCountry())
        .addressType(entity.getAddressType())
        .isDefault(entity.getIsDefault())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .version(entity.getVersion())
        .build();
  }

  public AddressEntity toEntity(Address address, UsersEntity userEntity) {
    return AddressEntity.builder()
        .user(userEntity)
        .street(address.getStreet())
        .number(address.getNumber())
        .complement(address.getComplement())
        .neighborhood(address.getNeighborhood())
        .city(address.getCity())
        .state(address.getState())
        .zipCode(address.getZipCode())
        .country(address.getCountry())
        .addressType(address.getAddressType())
        .isDefault(address.getIsDefault())
        .build();
  }
}
