package io.vividcode.happyride.address;


import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vividcode.happyride.address.AddressServiceTest.TestConfiguration;
import io.vividcode.happyride.address.domain.Address;
import io.vividcode.happyride.address.service.AddressService;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {TestApplication.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class, TestConfiguration.class})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
public class AddressServiceTest {

  @Autowired
  private AddressService addressService;

  @Test
  public void testSearchAddress() {
    List<Address> result = addressService.search(110101001015L, "王府井社区居委会");
    assertTrue(result.size() > 0);
    System.out.println(result);
  }

  @Configuration
  static class TestConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private ConfigurableEnvironment environment;

    @Bean(destroyMethod = "close")
    public DataSource testDataSource() {
      HikariConfig hikariConfig = new HikariConfig();
      hikariConfig.setDriverClassName(driverClassName);
      hikariConfig.setJdbcUrl(environment.resolvePlaceholders(jdbcUrl));
      hikariConfig.setUsername(environment.resolvePlaceholders(username));
      hikariConfig.setPassword(environment.resolvePlaceholders(password));
      return new HikariDataSource(hikariConfig);
    }
  }
}
