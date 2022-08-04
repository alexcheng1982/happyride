package io.vividcode.happyride.tripservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.playtika.test.common.spring.EmbeddedContainersShutdownAutoConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.events.TripCancellationResolutionRequiredEvent;
import io.vividcode.happyride.tripservice.api.events.TripCancelledEvent;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import io.vividcode.happyride.tripservice.api.web.TripVO;
import io.vividcode.happyride.tripservice.dataaccess.TripRepository;
import io.vividcode.happyride.tripservice.domain.Trip;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import io.vividcode.happyride.tripservice.domain.TripFareService;
import io.vividcode.happyride.tripservice.domain.TripService;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSaga;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
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
@ComponentScan(basePackageClasses = TripRepository.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class,
    TripService.class,
    TripFareService.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class,
    EmbeddedContainersShutdownAutoConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Trip service")
public class TripServiceTest {

  @Autowired
  TripService tripService;

  @Autowired
  TripFareService tripFareService;

  @MockBean
  TripDomainEventPublisher eventPublisher;

  @MockBean
  SagaInstanceFactory sagaInstanceFactory;

  @MockBean
  CreateTripSaga createTripSaga;

  @Captor
  private ArgumentCaptor<Trip> tripCaptor;

  @Captor
  private ArgumentCaptor<List<TripDomainEvent>> eventsCaptor;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("Cancel trip")
  public void testCancelTrip() {
    TripVO trip = this.tripService
        .createTrip(this.uuid(), this.position0(), this.position0());
    String tripId = trip.getId();
    this.tripService.shouldCancel(tripId, CancellationParty.PASSENGER);
    this.tripService.shouldCancel(tripId, CancellationParty.DRIVER);

    assertThat(this.tripService.getTrip(tripId))
        .hasValueSatisfying(t -> assertThat(t.getState()).isEqualTo(
            TripState.CANCELLED.name()));
    verify(this.eventPublisher, times(3))
        .publish(this.tripCaptor.capture(), this.eventsCaptor.capture());
    assertThat(this.eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripCancelledEvent.class);
  }

  @Test
  @DisplayName("Trip cancellation requires resolution")
  public void testCancelTripRequiresResolution() {
    TripVO trip = this.tripService
        .createTrip(this.uuid(), this.position0(), this.position0());
    String tripId = trip.getId();
    this.tripService.shouldCancel(tripId, CancellationParty.PASSENGER);
    this.tripService.shouldNotCancel(tripId, CancellationParty.DRIVER);

    assertThat(this.tripService.getTrip(tripId))
        .hasValueSatisfying(t -> assertThat(t.getState()).isEqualTo(
            TripState.CANCELLATION_REJECTED_BY_DRIVER.name()));
    verify(this.eventPublisher, times(3))
        .publish(this.tripCaptor.capture(), this.eventsCaptor.capture());
    assertThat(this.eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripCancellationResolutionRequiredEvent.class);
  }

  private String uuid() {
    return UUID.randomUUID().toString();
  }

  private PositionVO position0() {
    return new PositionVO(BigDecimal.ZERO, BigDecimal.ZERO);
  }
}
