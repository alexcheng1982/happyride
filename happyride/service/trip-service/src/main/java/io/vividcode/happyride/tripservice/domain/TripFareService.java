package io.vividcode.happyride.tripservice.domain;

import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.common.Utils;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class TripFareService {

  public BigDecimal calculate(final PositionVO startPos,
      final PositionVO endPos) {
    return BigDecimal.valueOf(3).multiply(
        BigDecimal.valueOf(Utils.calculateDistance(startPos, endPos)));
  }
}
