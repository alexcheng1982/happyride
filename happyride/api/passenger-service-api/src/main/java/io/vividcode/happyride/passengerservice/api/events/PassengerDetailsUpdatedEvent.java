package io.vividcode.happyride.passengerservice.api.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PassengerDetailsUpdatedEvent implements DomainEvent {

  @NonNull
  private PassengerDetails passengerDetails;
}
