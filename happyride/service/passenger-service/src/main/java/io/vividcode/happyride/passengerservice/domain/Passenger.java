package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Passenger extends BaseEntityWithGeneratedId {

  @Column(name = "name")
  @Size(max = 255)
  @NonNull
  private String name;

  @Column(name = "email")
  @Email
  @Size(max = 255)
  private String email;

  @Column(name = "mobile_phone_number")
  @Size(max = 255)
  @NonNull
  private String mobilePhoneNumber;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "passenger_id", referencedColumnName = "id", nullable = false)
  @NonNull
  private List<UserAddress> userAddresses = new ArrayList<>();

  public void addUserAddress(UserAddress userAddress) {
    if (userAddress != null) {
      userAddresses.add(userAddress);
    }
  }

  public void removeUserAddress(UserAddress userAddress) {
    userAddresses.remove(userAddress);
  }

  public void removeUserAddress(String addressId) {
    getUserAddress(addressId).ifPresent(this::removeUserAddress);
  }

  public Optional<UserAddress> getUserAddress(String addressId) {
    return userAddresses.stream()
        .filter(address -> Objects.equals(address.getId(), addressId))
        .findFirst();
  }
}
