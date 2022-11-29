package io.vividcode.happyride.tripservice.cqrs.dataaccess;

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
@Table(name = "trip_view")
@Data
public class TripView {

  @Id
  private String id;

  @Column(name = "start_pos_lng")
  private BigDecimal startPosLng;

  @Column(name = "start_pos_lat")
  private BigDecimal startPosLat;

  @Column(name = "end_pos_lng")
  private BigDecimal endPosLng;

  @Column(name = "ent_pos_lat")
  private BigDecimal endPosLat;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TripState state;
}
