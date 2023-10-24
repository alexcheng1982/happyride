package io.vividcode.happyride.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntityWithGeneratedId extends EntityWithGeneratedId {

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @PrePersist
  void setInitialDate() {
    createdAt = updatedAt = System.currentTimeMillis();
  }

  @PreUpdate
  void updateDate() {
    updatedAt = System.currentTimeMillis();
  }
}
