package io.vividcode.happyride.dispatchservice;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.dispatchservice.dataaccess.DispatchRepository;
import io.vividcode.happyride.dispatchservice.domain.Dispatch;
import io.vividcode.happyride.dispatchservice.domain.DispatchDomainEventPublisher;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = DispatchRepository.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class,
    DispatcherService.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
public class DispatcherServiceTest {

  @Autowired
  DispatcherService dispatcherService;

  @MockBean
  DispatchDomainEventPublisher eventPublisher;

  @MockBean
  DriverLocationService driverLocationService;

  @MockBean
  TripAcceptanceService tripAcceptanceService;

  @Test
  public void testDispatch() {
    Set<AvailableDriver> drivers = Sets.newHashSet(
        new AvailableDriver(TestUtils.uuid(), BigDecimal.ZERO, BigDecimal.ZERO),
        new AvailableDriver(TestUtils.uuid(), BigDecimal.ZERO, BigDecimal.ZERO)
    );
    when(driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(drivers);
    String tripId = TestUtils.uuid();
    dispatcherService.dispatchTrip(tripId, TestUtils.tripDetails0());
    ArgumentCaptor<Dispatch> captor1 = ArgumentCaptor.forClass(Dispatch.class);
    ArgumentCaptor<List> captor2 = ArgumentCaptor.forClass(List.class);
    verify(eventPublisher).publish(captor1.capture(), captor2.capture());
    assertEquals(tripId, captor1.getValue().getTripId());
    assertEquals(1, captor2.getValue().size());
  }
}
