package io.vividcode.happyride.common;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@MappedSuperclass
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
public abstract class EntityWithGeneratedId extends AbstractEntity<String> {

  @Id
  @Column(name = "id")
  @Size(max = 36)
  private String id;

  @PrePersist
  public void generateId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
  }
}
