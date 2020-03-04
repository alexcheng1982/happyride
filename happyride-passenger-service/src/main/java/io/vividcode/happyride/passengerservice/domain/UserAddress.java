package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.common.EntityWithGeneratedId;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
  private String name;

  @Column(name = "address_id", length = 36)
  private String addressId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "passenger_id", nullable = false)
  private Passenger passenger;
}
