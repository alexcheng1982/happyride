package io.vividcode.happyride.postgres.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class EmbeddedPostgresConfiguration {

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
