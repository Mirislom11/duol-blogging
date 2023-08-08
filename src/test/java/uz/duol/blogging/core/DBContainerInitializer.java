package uz.duol.blogging.core;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DBContainerInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final PostgreSQLContainer<?> sqlContainer =
      new PostgreSQLContainer<>("postgres:10.16-alpine")
          .withDatabaseName("integration-tests-db")
          .withUsername("username")
          .withPassword("password");

  static {
    sqlContainer.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    TestPropertyValues.of(
            "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
            "spring.datasource.username=" + sqlContainer.getUsername(),
            "spring.datasource.password=" + sqlContainer.getPassword())
        .applyTo(configurableApplicationContext.getEnvironment());
  }
}
