package com.connectfood.core.domain.model.commons;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel {
  private Long id;
  private String uuid;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer version;
}
