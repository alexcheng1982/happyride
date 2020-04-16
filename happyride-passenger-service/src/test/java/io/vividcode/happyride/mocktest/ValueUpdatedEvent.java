package io.vividcode.happyride.mocktest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValueUpdatedEvent {

  private Integer previousValue;

  private Integer currentValue;
}
