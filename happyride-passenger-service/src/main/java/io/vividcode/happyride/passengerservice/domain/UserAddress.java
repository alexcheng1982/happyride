package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.common.EntityWithGeneratedId;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@NoArgsConstructor
public class UserAddress extends EntityWithGeneratedId {

  @Column(name = "name")
  @Size(max = 255)
  private String name;

  @Column(name = "address_id")
  @Size(max = 36)
  private String addressId;
}
