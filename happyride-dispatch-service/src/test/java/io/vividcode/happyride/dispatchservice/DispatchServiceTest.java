package io.vividcode.happyride.dispatchservice;

import static io.vividcode.happyride.dispatchservice.TestUtils.acceptanceForDriver;
import static io.vividcode.happyride.dispatchservice.TestUtils.availableDriver0;
import static io.vividcode.happyride.dispatchservice.TestUtils.availableDriver1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
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
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("行程派发服务")
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
  @DisplayName("成功的派发")
  public void testSuccessfulDispatch() {
    Set<AvailableDriver> drivers = Sets.newHashSet(
        availableDriver0(),
        availableDriver1()
    );
    when(driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(drivers);
    String tripId = TestUtils.uuid();
    dispatchService.dispatchTrip(tripId, TestUtils.tripDetails0());
    verify(eventPublisher).publish(dispatchCaptor.capture(), eventsCaptor.capture());

    assertThat(dispatchCaptor.getValue().getTripId()).isEqualTo(tripId);
    assertThat(eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripDispatchedEvent.class);
    assertThat(dispatchService.findCurrentDispatchByTrip(tripId)).isPresent();
  }

  @Test
  @DisplayName("失败的派发")
  public void testFailedDispatch() {
    when(driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(Sets.newHashSet());
    String tripId = TestUtils.uuid();
    dispatchService.dispatchTrip(tripId, TestUtils.tripDetails0());
    verify(eventPublisher).publish(dispatchCaptor.capture(), eventsCaptor.capture());

    assertThat(dispatchCaptor.getValue().getTripId()).isEqualTo(tripId);
    assertThat(eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripDispatchFailedEvent.class);
    assertThat(dispatchService.findCurrentDispatchByTrip(tripId)).isNotPresent();
  }

  @Test
  @DisplayName("司机接受行程")
  public void testDriverAcceptance() {
    AvailableDriver driver1 = availableDriver0();
    AvailableDriver driver2 = availableDriver1();
    Set<AvailableDriver> drivers = Sets.newHashSet(driver1, driver2);
    when(driverLocationService.findAvailableDrivers(BigDecimal.ZERO, BigDecimal.ZERO))
        .thenReturn(drivers);
    String tripId = TestUtils.uuid();
    dispatchService.dispatchTrip(tripId, TestUtils.tripDetails0());
    dispatchService.submitTripAcceptance(tripId, acceptanceForDriver(driver1));
    dispatchService.submitTripAcceptance(tripId, acceptanceForDriver(driver2));
    dispatchService.selectTripAcceptance(tripId, driver1.getDriverId());
    verify(eventPublisher, times(2)).publish(dispatchCaptor.capture(), eventsCaptor.capture());
    List<List<DispatchDomainEvent>> allValues = eventsCaptor.getAllValues();
    assertThat(allValues).hasSize(2);
    assertThat(allValues).element(0).asList().hasSize(1)
        .hasOnlyElementsOfType(TripDispatchedEvent.class);
    assertThat(allValues)
        .element(1).asList().hasSize(2).hasOnlyElementsOfTypes(TripAcceptanceSelectedEvent.class,
        TripAcceptanceDeclinedEvent.class);
  }
}
