package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.tripservice.api.TripState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trip_history")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TripHistory {
  @Column(name = "trip_id")
  private String tripId;



  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TripState state;
}
