package com.connectfood.core.infrastructure.persistence.entity.commons;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
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
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, updatable = false,
      columnDefinition = "uuid default gen_random_uuid()")
  private String uuid;

  @Column(name = "created_at", nullable = false,
      columnDefinition = "timestamp default current_timestamp",
      updatable = false, insertable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false,
      columnDefinition = "timestamp default current_timestamp",
      insertable = false, updatable = false)
  private String updatedAt;

  @Version
  @Column(nullable = false)
  private Long version;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID()
        .toString();
  }
}
