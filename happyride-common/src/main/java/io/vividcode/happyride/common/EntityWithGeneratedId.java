package io.vividcode.happyride.common;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@MappedSuperclass
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
public abstract class EntityWithGeneratedId extends AbstractEntity<String> {
  @Id
  @Column(length = 36)
  private String id;

  @PrePersist
  void generateId() {
    id = UUID.randomUUID().toString();
  }
}
