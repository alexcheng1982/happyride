package io.vividcode.happyride.addressservice;


import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackageClasses = {AddressRepository.class, AddressService.class})
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class}
)
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Address Service")
public class AddressServiceTest {

  @Autowired
  AddressService addressService;

  @Test
  @DisplayName("Search address")
  public void testSearchAddress() {
    final List<AddressVO> result = this.addressService.search(110101001015L, "王府井社区居委会");
    assertTrue(result.size() > 0);
  }
}
