package io.vividcode.happyride.dispatchservice.domain;

import io.vividcode.happyride.common.EntityWithGeneratedId;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trip_acceptances")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class TripAcceptance extends EntityWithGeneratedId {

  @NonNull
  @Column(name = "driver_id")
  private String driverId;

  @NonNull
  @Column(name = "current_pos_lng")
  private BigDecimal currentPosLng;

  @NonNull
  @Column(name = "current_pos_lat")
  private BigDecimal currentPosLat;

  @NonNull
  private Long timestamp = System.currentTimeMillis();

  @NonNull
  @Enumerated(EnumType.STRING)
  private TripAcceptanceState state = TripAcceptanceState.INVITED;
}
