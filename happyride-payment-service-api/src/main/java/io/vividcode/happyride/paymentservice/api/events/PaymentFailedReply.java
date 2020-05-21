package io.vividcode.happyride.paymentservice.api.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PaymentFailedReply {

  @NonNull
  private String reason;
}
