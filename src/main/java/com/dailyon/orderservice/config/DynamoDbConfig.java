package com.dailyon.orderservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import static com.dailyon.orderservice.config.DynamoDbConfig.*;

@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackages = DYNAMO_DOMAIN_PACKAGE)
public class DynamoDbConfig {

  static final String DYNAMO_DOMAIN_PACKAGE = "com.dailyon.orderservice.domain.torder.repository";

  @Value("${cloud.aws.dynamodb.endpoint:test}")
  private String amazonDynamoDBEndpoint;

  @Value("${cloud.aws.credentials.ACCESS_KEY_ID:test}")
  private String amazonAWSAccessKey;

  @Value("${cloud.aws.credentials.SECRET_ACCESS_KEY:test}")
  private String amazonAWSSecretKey;

  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
  }

  public AWSCredentialsProvider amazonAWSCredentialsProvider() {
    return new AWSStaticCredentialsProvider(amazonAWSCredentials());
  }

  @Bean
  @Primary
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    return DynamoDBMapperConfig.DEFAULT;
  }

  @Bean
  @Primary
  AmazonDynamoDB amazonDynamoDB() {
    AwsClientBuilder.EndpointConfiguration endpointConfiguration =
        new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "");

    return AmazonDynamoDBClientBuilder.standard()
        .withCredentials(amazonAWSCredentialsProvider())
        .withEndpointConfiguration(endpointConfiguration)
        .build();
  }

  @Bean
  @Primary
  public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    return new DynamoDBMapper(amazonDynamoDB, config);
  }

  public static class LocalDateTimeConverter implements DynamoDBTypeConverter<Date, LocalDateTime> {
    @Override
    public Date convert(LocalDateTime source) {
      return Date.from(source.toInstant(ZoneOffset.UTC));
    }

    @Override
    public LocalDateTime unconvert(Date source) {
      return LocalDateTime.ofInstant(source.toInstant(), ZoneId.of("UTC"));
    }
  }
}
