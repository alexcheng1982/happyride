package io.vividcode.happyride.driverservice.model;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vehicle extends BaseEntityWithGeneratedId {

  @Column(name = "make")
  @Size(max = 60)
  private String make;

  @Column(name = "mode")
  @Size(max = 60)
  private String mode;

  @Column(name = "year")
  private Integer year;

  @Column(name = "registration")
  @Size(max = 32)
  private String registration;
}
