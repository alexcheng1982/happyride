package io.vividcode.happyride.addressservice.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AreaService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
@DisplayName("Address resource")
public class AddressResourceTest {

  @BeforeAll
  public static void setup() {
    AddressService addressService = Mockito.mock(AddressService.class);
    Mockito.when(addressService.search(anyLong(), anyString())).thenReturn(
        Collections.singletonList(createAddress()));
    QuarkusMock.installMockForType(addressService, AddressService.class);

    QuarkusMock
        .installMockForType(Mockito.mock(AreaService.class), AreaService.class);
  }

  @Test
  @DisplayName("search")
  public void testSearch() {
    given()
        .when()
        .queryParam("areaCode", "1")
        .queryParam("query", "test")
        .get("/search")
        .then()
        .statusCode(200)
        .body("$", hasSize(1));
  }

  private static AddressVO createAddress() {
    AddressVO address = new AddressVO();
    address.setId(UUID.randomUUID().toString());
    address.setAreaId(0);
    address.setAddressLine("Test");
    address.setLat(BigDecimal.ZERO);
    address.setLng(BigDecimal.ONE);
    return address;
  }
}
