package com.dailyon.orderservice;

import org.junit.ClassRule;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

public class ContainerBaseTestSupport extends IntegrationTestSupport {

  private static final String DOCKER_DYNAMODB_IMAGE = "amazon/dynamodb-local:1.24.0";

  @ClassRule public static GenericContainer DYNAMODB_CONTAINER;

  static {
    DYNAMODB_CONTAINER =
        new GenericContainer<>(DOCKER_DYNAMODB_IMAGE).withExposedPorts(8000).withReuse(true);
    DYNAMODB_CONTAINER.start();
  }

  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry registry) {

    // dynamo
    final String endpoint =
        String.format(
            "http://%s:%s", DYNAMODB_CONTAINER.getHost(), DYNAMODB_CONTAINER.getMappedPort(8000));
    registry.add("amazon.dynamodb.endpoint", () -> endpoint);
    registry.add("amazon.aws.accesskey", () -> "testkey");
    registry.add("amazon.aws.secretkey", () -> "testkey");
  }
}
