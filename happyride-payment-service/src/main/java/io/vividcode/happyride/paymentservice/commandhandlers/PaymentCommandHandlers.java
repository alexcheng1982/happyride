package io.vividcode.happyride.paymentservice.commandhandlers;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.paymentservice.api.PaymentServiceChannels;
import io.vividcode.happyride.paymentservice.api.events.CreatePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.MakePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.PaymentFailedReply;
import io.vividcode.happyride.paymentservice.domain.PaymentResult;
import io.vividcode.happyride.paymentservice.domain.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentCommandHandlers {

  @Autowired
  PaymentService paymentService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(PaymentServiceChannels.payment)
        .onMessage(CreatePaymentCommand.class, this::createPayment)
        .onMessage(MakePaymentCommand.class, this::makePayment)
        .build();
  }

  private Message createPayment(final CommandMessage<CreatePaymentCommand> cm) {
    final CreatePaymentCommand command = cm.getCommand();
    this.paymentService.createPayment(command.getTripId(), command.getAmount());
    return withSuccess();
  }

  private Message makePayment(final CommandMessage<MakePaymentCommand> cm) {
    try {
      final PaymentResult paymentResult = this.paymentService
          .makePayment(cm.getCommand().getTripId());
      return paymentResult.isSuccess() ? withSuccess() :
          withFailure(new PaymentFailedReply(paymentResult.getErrorMessage()));
    } catch (final Exception e) {
      return withFailure(new PaymentFailedReply(e.getMessage()));
    }
  }
}
