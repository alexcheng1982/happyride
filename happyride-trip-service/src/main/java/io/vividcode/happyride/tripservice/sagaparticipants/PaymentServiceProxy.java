package io.vividcode.happyride.tripservice.sagaparticipants;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import io.vividcode.happyride.paymentservice.api.PaymentServiceChannels;
import io.vividcode.happyride.paymentservice.api.events.CreatePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.MakePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.PaymentFailedReply;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceProxy {

  public final CommandEndpoint<CreatePaymentCommand> createPayment = CommandEndpointBuilder
      .forCommand(CreatePaymentCommand.class)
      .withChannel(PaymentServiceChannels.payment)
      .withReply(Success.class)
      .build();

  public final CommandEndpoint<MakePaymentCommand> makePayment = CommandEndpointBuilder
      .forCommand(MakePaymentCommand.class)
      .withChannel(PaymentServiceChannels.payment)
      .withReply(Success.class)
      .withReply(PaymentFailedReply.class)
      .build();
}
