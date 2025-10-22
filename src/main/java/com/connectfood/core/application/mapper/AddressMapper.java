package com.connectfood.core.application.mapper;

import java.time.ZoneId;
import java.util.List;

import com.connectfood.core.domain.model.Address;
import com.connectfood.model.AddressResponse;
import com.connectfood.model.AddressUpsertRequest;

import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

  public AddressResponse toResponse(Address address) {

    AddressResponse response = new AddressResponse();
    response.setUuid(address.getUuid());
    response.setStreet(address.getStreet());
    response.setNumber(address.getNumber());
    response.setComplement(address.getComplement());
    response.setNeighborhood(address.getNeighborhood());
    response.setCity(address.getCity());
    response.setState(address.getState());
    response.setCountry(address.getCountry());
    response.setZipCode(address.getZipCode());
    response.setAddressType(address.getAddressType());
    response.setIsDefault(address.getIsDefault());
    response.setCreatedAt(address.getCreatedAt()
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime());
    response.setLastUpdateAt(address.getUpdatedAt()
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime());
    return response;
  }

  public List<AddressResponse> toResponses(List<Address> addresses) {
    return addresses.stream()
        .map(this::toResponse)
        .toList();
  }

  public Address create(AddressUpsertRequest request) {
    return Address.builder()
        .street(request.getStreet())
        .number(request.getNumber())
        .complement(request.getComplement())
        .neighborhood(request.getNeighborhood())
        .city(request.getCity())
        .state(request.getState())
        .country(request.getCountry())
        .zipCode(request.getZipCode())
        .addressType(request.getAddressType())
        .isDefault(request.getIsDefault())
        .build();
  }

}
