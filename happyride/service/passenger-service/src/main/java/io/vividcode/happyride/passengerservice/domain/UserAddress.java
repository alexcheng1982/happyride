package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.common.EntityWithGeneratedId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
