package io.vividcode.happyride.postgres.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class EmbeddedPostgresConfiguration {

  @Autowired
  ConfigurableEnvironment environment;

  @Bean(destroyMethod = "close")
  public DataSource testDataSource() {
    String jdbcUrl = "jdbc:postgresql://${embedded.postgresql.host}:${embedded.postgresql.port}/${embedded.postgresql.schema}";
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName("org.postgresql.Driver");
    hikariConfig.setJdbcUrl(environment.resolvePlaceholders(jdbcUrl));
    hikariConfig.setUsername(environment.getProperty("embedded.postgresql.user"));
    hikariConfig.setPassword(environment.getProperty("embedded.postgresql.password"));
    return new HikariDataSource(hikariConfig);
  }
}
