package io.vividcode.happyride.paymentservice.api.events;

import io.eventuate.tram.commands.common.Command;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreatePaymentCommand implements Command {

  @NonNull
  private String tripId;

  @NonNull
  private BigDecimal amount;
}
