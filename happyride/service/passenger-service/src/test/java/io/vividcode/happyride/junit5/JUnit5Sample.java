package io.vividcode.happyride.junit5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("JUnit 5 sample")
public class JUnit5Sample {

  @ParameterizedTest
  @ValueSource(strings = {"hello", "world"})
  @DisplayName("String length")
  void stringLength(String value) {
    assertThat(value).hasSize(5);
  }

  @RepeatedTest(3)
  void repeatedTest() {
    assertThat(true).isTrue();
  }

  @TestFactory
  DynamicTest[] dynamicTests() {
    return new DynamicTest[]{
        dynamicTest("Dynamic test 1", () ->
            assertThat(10).isGreaterThan(5)),
        dynamicTest("Dynamic test 2", () ->
            assertThat("hello").hasSize(5))
    };
  }
}
