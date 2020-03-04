package io.vividcode.happyride.passengerservice.domain;

import com.google.common.collect.Lists;
import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.User;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Passenger extends BaseEntityWithGeneratedId {

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  @Email
  private String email;

  @Column(name = "mobile_phone_number")
  private String mobilePhoneNumber;

  @OneToMany(
      mappedBy = "passenger",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<UserAddress> userAddresses = new ArrayList<>();

  public void addUserAddress(UserAddress userAddress) {
    userAddresses.add(userAddress);
    if (userAddress.getPassenger() != null) {
      userAddress.setPassenger(this);
    }
  }

  public void removeAddress(UserAddress userAddress) {
    userAddresses.remove(userAddress);
    userAddress.setPassenger(null);
  }

  public Optional<UserAddress> getUserAddress(String addressId) {
    return userAddresses.stream()
        .filter(address -> Objects.equals(address.getId(), addressId))
        .findFirst();
  }
}
