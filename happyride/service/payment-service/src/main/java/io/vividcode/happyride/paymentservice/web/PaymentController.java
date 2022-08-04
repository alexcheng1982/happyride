package io.vividcode.happyride.paymentservice.web;

import io.vividcode.happyride.paymentservice.api.web.MakePaymentRequest;
import io.vividcode.happyride.paymentservice.domain.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  @Autowired
  PaymentService paymentService;

  @PostMapping
  public void makePayment(@RequestBody final MakePaymentRequest request) {
    this.paymentService.makePayment(request.getTripId());
  }
}
