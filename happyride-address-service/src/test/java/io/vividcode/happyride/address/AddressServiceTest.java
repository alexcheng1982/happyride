package io.vividcode.happyride.address;


import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import io.vividcode.happyride.address.domain.Address;
import io.vividcode.happyride.address.service.AddressService;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {TestApplication.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class, EmbeddedPostgresConfiguration.class})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
public class AddressServiceTest {

  @Autowired
  AddressService addressService;

  @Test
  public void testSearchAddress() {
    List<Address> result = addressService.search(110101001015L, "王府井社区居委会");
    assertTrue(result.size() > 0);
  }
}
