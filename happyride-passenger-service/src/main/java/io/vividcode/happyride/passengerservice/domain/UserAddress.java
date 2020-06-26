package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.common.EntityWithGeneratedId;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@NoArgsConstructor
public class UserAddress extends EntityWithGeneratedId {

  @Column(name = "name")
  @Size(max = 255)
  @NonNull
  private String name;

  @Column(name = "address_id")
  @Size(max = 36)
  @NonNull
  private String addressId;
}
