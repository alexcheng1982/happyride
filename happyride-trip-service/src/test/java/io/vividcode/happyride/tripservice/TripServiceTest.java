package io.vividcode.happyride.tripservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.events.TripCancellationResolutionRequiredEvent;
import io.vividcode.happyride.tripservice.api.events.TripCancelledEvent;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import io.vividcode.happyride.tripservice.api.web.TripView;
import io.vividcode.happyride.tripservice.dataaccess.TripRepository;
import io.vividcode.happyride.tripservice.domain.Trip;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSagaState;
import io.vividcode.happyride.tripservice.service.TripService;
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
    TripService.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("行程服务")
public class TripServiceTest {

  @Autowired
  TripService tripService;

  @MockBean
  TripDomainEventPublisher eventPublisher;

  @MockBean
  SagaManager<CreateTripSagaState> sagaManager;

  @Captor
  private ArgumentCaptor<Trip> tripCaptor;

  @Captor
  private ArgumentCaptor<List<TripDomainEvent>> eventsCaptor;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("行程取消成功")
  public void testCancelTrip() {
    TripView trip = tripService.createTrip(uuid(), position0(), position0());
    String tripId = trip.getId();
    tripService.shouldCancel(tripId, CancellationParty.PASSENGER);
    tripService.shouldCancel(tripId, CancellationParty.DRIVER);

    assertThat(tripService.getTrip(tripId))
        .hasValueSatisfying(t -> assertThat(t.getState()).isEqualTo(
            TripState.CANCELLED));
    verify(eventPublisher, times(3))
        .publish(tripCaptor.capture(), eventsCaptor.capture());
    assertThat(eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripCancelledEvent.class);
  }

  @Test
  @DisplayName("行程取消需要调解")
  public void testCancelTripRequiresResolution() {
    TripView trip = tripService.createTrip(uuid(), position0(), position0());
    String tripId = trip.getId();
    tripService.shouldCancel(tripId, CancellationParty.PASSENGER);
    tripService.shouldNotCancel(tripId, CancellationParty.DRIVER);

    assertThat(tripService.getTrip(tripId))
        .hasValueSatisfying(t -> assertThat(t.getState()).isEqualTo(
            TripState.CANCELLATION_REJECTED_BY_DRIVER));
    verify(eventPublisher, times(3))
        .publish(tripCaptor.capture(), eventsCaptor.capture());
    assertThat(eventsCaptor.getValue()).hasSize(1)
        .hasOnlyElementsOfType(TripCancellationResolutionRequiredEvent.class);
  }

  private String uuid() {
    return UUID.randomUUID().toString();
  }

  private PositionView position0() {
    return new PositionView(BigDecimal.ZERO, BigDecimal.ZERO);
  }
}
