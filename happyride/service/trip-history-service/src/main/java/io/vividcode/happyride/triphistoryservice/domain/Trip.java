package io.vividcode.happyride.triphistoryservice.domain;

import io.vividcode.happyride.tripservice.api.TripState;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "trips")
@Data
public class Trip {

  @Id
  private String id;

  @Column(name = "passenger_id")
  private String passengerId;

  @Column(name = "passenger_name")
  private String passengerName;

  @Column(name = "driver_id")
  private String driverId;

  @Column(name = "driver_name")
  private String driverName;

  @Column(name = "start_pos_lng")
  private BigDecimal startPosLng;

  @Column(name = "start_pos_lat")
  private BigDecimal startPosLat;

  @Column(name = "end_pos_lng")
  private BigDecimal endPosLng;

  @Column(name = "end_pos_lat")
  private BigDecimal endPosLat;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TripState state;
}
