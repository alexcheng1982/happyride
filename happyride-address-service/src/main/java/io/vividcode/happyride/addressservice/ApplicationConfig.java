package io.vividcode.happyride.addressservice;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class ApplicationConfig {
  public TimedAspect timedAspect(final MeterRegistry meterRegistry) {
    return new TimedAspect(meterRegistry);
  }
}
