package io.vividcode.happyride.paymentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {

  @NonNull
  private boolean success;

  private String errorMessage;

  public static PaymentResult SUCCESS = new PaymentResult(true);

  public static PaymentResult failure(final String errorMessage) {
    return new PaymentResult(false, errorMessage);
  }
}
