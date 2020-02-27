package io.vividcode.happyride.dispatcherservice.domain;

import io.vividcode.happyride.common.EntityWithGeneratedId;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dispatches")
public class Dispatch extends EntityWithGeneratedId {
  private String tripId;

  private BigDecimal lng;

  private BigDecimal lat;
}
