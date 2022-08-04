package io.vividcode.happyride.paymentservice.domain;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Payment extends BaseEntityWithGeneratedId {

  @NonNull
  @Column(name = "trip_id")
  @Size(max = 36)
  private String tripId;

  @Column(name = "amount")
  @NonNull
  private BigDecimal amount;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private PaymentState state = PaymentState.PENDING;
}
