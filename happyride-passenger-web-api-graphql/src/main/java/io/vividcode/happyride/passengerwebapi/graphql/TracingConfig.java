package io.vividcode.happyride.passengerwebapi.graphql;

import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

  @Bean
  public Tracer tracer() {
    return io.jaegertracing.Configuration.fromEnv().getTracer();
  }

  @Bean
  public FilterRegistrationBean<TracingFilter> tracingFilter(Tracer tracer) {
    return new FilterRegistrationBean<>(new TracingFilter(tracer));
  }
}
