package dev.ta2khu75.shopapp.configurations;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
@Configuration
public class FlywayConfig {
  @Value("${spring.flyway.locations}")
  private String[] flywayLocations;
  @Value("${spring.datasource.url}")
  private String url;
  @Value("${spring.datasource.password}")
  private String password;
  @Value("${spring.datasource.username}")
  private String username;

  @Bean
  public Flyway flyway(){
    Flyway flyway=Flyway.configure().dataSource(this.dataSource()).locations(flywayLocations).baselineOnMigrate(true).baselineVersion("0").load();
    flyway.migrate();
    System.out.println("migrating..........");
    return flyway;
  }

  @Bean
  public DataSource dataSource(){
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

}
