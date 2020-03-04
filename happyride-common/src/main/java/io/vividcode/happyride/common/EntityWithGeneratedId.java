package io.vividcode.happyride.common;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;
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
