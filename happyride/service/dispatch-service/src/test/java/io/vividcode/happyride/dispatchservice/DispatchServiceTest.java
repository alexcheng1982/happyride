package io.vividcode.happyride.dispatchservice;

import static io.vividcode.happyride.dispatchservice.TestUtils.acceptanceForDriver;
import static io.vividcode.happyride.dispatchservice.TestUtils.availableDriver0;
import static io.vividcode.happyride.dispatchservice.TestUtils.availableDriver1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.playtika.test.common.spring.EmbeddedContainersShutdownAutoConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.dispatchservice.api.events.DispatchDomainEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceDeclinedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceSelectedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchedEvent;
import io.vividcode.happyride.dispatchservice.dataaccess.DispatchRepository;
import io.vividcode.happyride.dispatchservice.domain.Dispatch;
import io.vividcode.happyride.dispatchservice.domain.DispatchDomainEventPublisher;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = DispatchRepository.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class,
    DispatchService.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class,
    EmbeddedContainersShutdownAutoConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine",
})
@DisplayName("Dispatch service")
public class DispatchServiceTest {

  @Autowired
  DispatchService dispatchService;

  @MockBean
  DispatchDomainEventPublisher eventPublisher;

  @MockBean
  DriverLocationService driverLocationService;

  @MockBean
  TripAcceptanceService tripAcceptanceService;

  @Captor
  private ArgumentCaptor<Dispatch> dispatchCaptor;

  @Captor
  private ArgumentCaptor<List<DispatchDomainEvent>> eventsCaptor;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("Successful dispatch")
  public void testSuccessfulDispatch() {
    Set<AvailableDriver> drivers = Sets.newHashSet(
        availableDriver0(),
        availableDriver1()
    );
    when(this.driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(drivers);
    String tripId = TestUtils.uuid();
    this.dispatchService.dispatchTrip(tripId, TestUtils.tripDetails0());
    verify(this.eventPublisher).publish(this.dispatchCaptor.capture(), this.eventsCaptor.capture());

    assertThat(this.dispatchCaptor.getValue().getTripId()).isEqualTo(tripId);
    assertThat(this.eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripDispatchedEvent.class);
    assertThat(this.dispatchService.findCurrentDispatchByTrip(tripId)).isPresent();
  }

  @Test
  @DisplayName("Failed dispatch")
  public void testFailedDispatch() {
    when(this.driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(Sets.newHashSet());
    String tripId = TestUtils.uuid();
    this.dispatchService.dispatchTrip(tripId, TestUtils.tripDetails0());
    verify(this.eventPublisher).publish(this.dispatchCaptor.capture(), this.eventsCaptor.capture());

    assertThat(this.dispatchCaptor.getValue().getTripId()).isEqualTo(tripId);
    assertThat(this.eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripDispatchFailedEvent.class);
    assertThat(this.dispatchService.findCurrentDispatchByTrip(tripId)).isNotPresent();
  }

  @Test
  @DisplayName("Driver accepts trip")
  public void testDriverAcceptance() {
    AvailableDriver driver1 = availableDriver0();
    AvailableDriver driver2 = availableDriver1();
    Set<AvailableDriver> drivers = Sets.newHashSet(driver1, driver2);
    when(this.driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(drivers);
    String tripId = TestUtils.uuid();
    this.dispatchService.dispatchTrip(tripId, TestUtils.tripDetails0());
    this.dispatchService.submitTripAcceptance(tripId, acceptanceForDriver(driver1));
    this.dispatchService.submitTripAcceptance(tripId, acceptanceForDriver(driver2));
    this.dispatchService.selectTripAcceptance(tripId, driver1.getDriverId());
    verify(this.eventPublisher, times(2))
        .publish(this.dispatchCaptor.capture(), this.eventsCaptor.capture());
    List<List<DispatchDomainEvent>> allValues = this.eventsCaptor.getAllValues();
    assertThat(allValues).hasSize(2);
    assertThat(allValues).element(0).asList().hasSize(1)
        .hasOnlyElementsOfType(TripDispatchedEvent.class);
    assertThat(allValues)
        .element(1).asList().hasSize(2).hasOnlyElementsOfTypes(TripAcceptanceSelectedEvent.class,
            TripAcceptanceDeclinedEvent.class);
  }
}
