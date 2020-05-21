package io.vividcode.happyride.paymentservice.domain;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class PaymentGateway {

  private static final BigDecimal PAYMENT_LIMIT = BigDecimal.valueOf(200);

  public void makePayment(final BigDecimal amount) {
    if (PAYMENT_LIMIT.compareTo(amount) < 0) {
      throw new PaymentException(String
          .format("Amount too large, requested %s, limit %s", amount,
              PAYMENT_LIMIT));
    }
  }
}
