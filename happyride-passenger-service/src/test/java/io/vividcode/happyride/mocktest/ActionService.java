package io.vividcode.happyride.mocktest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {

  @Autowired
  ValueUpdater valueUpdater;

  @Autowired
  EventPublisher eventPublisher;

  public int performAction(Integer value) {
    Integer oldValue = valueUpdater.getValue();
    if (valueUpdater.updateValue(value)) {
      ValueUpdatedEvent event = new ValueUpdatedEvent(oldValue, value);
      eventPublisher.publishEvent(event);
      return value * 10;
    }
    return 0;
  }
}
