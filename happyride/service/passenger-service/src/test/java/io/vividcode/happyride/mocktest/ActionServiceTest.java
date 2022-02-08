package io.vividcode.happyride.mocktest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@DisplayName("Action service test")
public class ActionServiceTest {

  @Autowired
  ActionService actionService;

  @MockBean
  ValueUpdater valueUpdater;

  @MockBean
  EventPublisher eventPublisher;

  @Captor
  ArgumentCaptor<ValueUpdatedEvent> eventCaptor;

  @Test
  @DisplayName("Value updated")
  public void testValueUpdated() {
    int value = 10;
    given(valueUpdater.updateValue(value)).willReturn(true);
    assertThat(actionService.performAction(value)).isEqualTo(100);
    verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
    assertThat(eventCaptor.getValue()).extracting(ValueUpdatedEvent::getCurrentValue)
        .isEqualTo(value);
  }

  @Test
  @DisplayName("Value not updated")
  public void testValueNotUpdated() {
    int value = 10;
    given(valueUpdater.updateValue(value)).willReturn(false);
    assertThat(actionService.performAction(value)).isEqualTo(0);
    verify(eventPublisher, never()).publishEvent(eventCaptor.capture());
  }
}
