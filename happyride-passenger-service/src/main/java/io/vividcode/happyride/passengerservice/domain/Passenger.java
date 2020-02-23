package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.common.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "passenger")
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Passenger extends AbstractEntity<String> {
  @Id
  private String id;

  private String name;

  private String email;

  private String mobilePhoneNumber;
}
