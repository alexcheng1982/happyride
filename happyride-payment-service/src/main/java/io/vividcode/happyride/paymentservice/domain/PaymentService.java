package io.vividcode.happyride.paymentservice.domain;

import io.vividcode.happyride.paymentservice.dataaccess.PaymentRepository;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentService {

  @Autowired
  PaymentGateway paymentGateway;

  @Autowired
  PaymentRepository paymentRepository;

  public void createPayment(final String tripId, final BigDecimal amount) {
    final Payment payment = new Payment();
    payment.setTripId(tripId);
    payment.setAmount(amount);
    this.paymentRepository.save(payment);
  }

  public PaymentResult makePayment(final String tripId) {
    final Payment payment = this.paymentRepository.findByTripId(tripId)
        .orElseThrow(() -> new PaymentNotFoundException(tripId));
    try {
      this.paymentGateway.makePayment(payment.getAmount());
      payment.setState(PaymentState.PAYED);
      return PaymentResult.SUCCESS;
    } catch (final PaymentException e) {
      payment.setState(PaymentState.FAILED);
      return PaymentResult.failure(e.getMessage());
    } finally {
      this.paymentRepository.save(payment);
    }
  }
}
