package io.vividcode.happyride.driverservice.model;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import io.vividcode.happyride.common.DriverState;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Driver extends BaseEntityWithGeneratedId {

  @Column(name = "name")
  @Size(max = 255)
  private String name;

  @Column(name = "email")
  @Email
  @Size(max = 255)
  private String email;

  @Column(name = "mobile_phone_number")
  @Size(max = 255)
  private String mobilePhoneNumber;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
  private List<Vehicle> vehicles = new ArrayList<>();

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private DriverState state = DriverState.OFFLINE;

  public void addVehicle(Vehicle vehicle) {
    if (vehicle != null) {
      vehicles.add(vehicle);
    }
  }
}
