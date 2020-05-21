package io.vividcode.happyride.paymentservice.api.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class MakePaymentRequest {

  @NonNull
  private String tripId;
}
