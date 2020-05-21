package io.vividcode.happyride.paymentservice.api.events;

import io.eventuate.tram.commands.common.Command;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class MakePaymentCommand implements Command {

  @NonNull
  private String tripId;
}
