package io.vividcode.happyride.paymentservice.domain;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
