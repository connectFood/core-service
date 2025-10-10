package com.connectfood.core.infrastructure.persistence.entity;

import com.connectfood.core.infrastructure.persistence.entity.commons.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AddressEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UsersEntity userId;

  @Column(nullable = false)
  private String street;

  @Column(nullable = false)
  private String number;

  @Column()
  private String complement;

  @Column()
  private String neighborhood;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String state;

  @Column(name = "zip_code", nullable = false)
  private String zipCode;

  @Column(nullable = false)
  private String country;

  @Column(name = "address_type")
  private String addressType;

  @Column(name = "is_default", nullable = false)
  private Boolean isDefault;
}
