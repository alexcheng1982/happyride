package io.vividcode.happyride.mocktest;

import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

  public void publishEvent(ValueUpdatedEvent event) {
    System.out.println(event);
  }
}
