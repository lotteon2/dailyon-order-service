package com.dailyon.orderservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackages = {"com.dailyon.orderservice.dynamodb.repository"})
public class DynamoDbConfig {

  @Bean
  @Primary
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    return DynamoDBMapperConfig.DEFAULT;
  }

  @Profile({"!test"})
  @Bean
  @Primary
  public AmazonDynamoDB amazonDynamoDB() {
    log.info("Start AWS Amazon DynamoDB Client");
    return AmazonDynamoDBClientBuilder.standard()
        .withCredentials(InstanceProfileCredentialsProvider.getInstance()) // (3)
        .withRegion(Regions.AP_NORTHEAST_2)
        .build();
  }

  @Profile({"test", "local"})
  @Bean(name = "amazonDynamoDB")
  @Primary
  public AmazonDynamoDB localAmazonDynamoDB() {
    log.info("Start Local Amazon DynamoDB Client");
    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("test", "test");
    return AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)) // (5)
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:8001", Regions.AP_NORTHEAST_2.getName())) // (6)
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
      return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime();
    }
  }
}
