package io.vividcode.happyride.mocktest;

import org.springframework.stereotype.Service;

@Service
public class ValueUpdater {

  private Integer value;

  public boolean updateValue(Integer value) {
    if (value != null && value > 0) {
      this.value = value;
      return true;
    }
    return false;
  }

  public Integer getValue() {
    return value;
  }
}
