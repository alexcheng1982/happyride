package io.vividcode.happyride.tripservice.messagehandlers;

import com.google.common.collect.ImmutableSet;
import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.vividcode.happyride.dispatcherservice.api.events.MessageDestination;
import io.vividcode.happyride.tripservice.api.events.TripDispatchedEvent;
import io.vividcode.happyride.tripservice.domain.TripState;
import io.vividcode.happyride.tripservice.service.TripService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripMessageHandlers {
  @Autowired
  MessageConsumer messageConsumer;

  @Autowired
  TripService tripService;

  private String subscriberId = "subscriberId_" + System.currentTimeMillis();

  @PostConstruct
  public void initialize() {
    messageConsumer.subscribe(subscriberId, ImmutableSet.of(MessageDestination.TRIP_DISPATCHED), this::messageHandler);
  }

  public void messageHandler(Message message) {
    TripDispatchedEvent event = JSonMapper
        .fromJson(message.getPayload(), TripDispatchedEvent.class);
    tripService.updateTripState(event.getTripId(), TripState.DISPATCHED);
  }
}
