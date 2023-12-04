package com.dailyon.orderservice.domain.order.entity.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "embedded-dynamodb.use=true")
@ActiveProfiles("test")
class Order {

  @Autowired private AmazonDynamoDB dynamoDB;

  @Test
  @DisplayName("dynamodb order table 생성 테스트")
  void test_createTable() throws Exception {
    // given
    String tableName = "order";
    String hashKeyName = "order_id";

    // when
    CreateTableResult res = createTable(tableName, hashKeyName);

    // then
    TableDescription tableDesc = res.getTableDescription();
    assertThat(tableDesc.getTableName()).isEqualTo(tableName);
    assertThat(tableDesc.getKeySchema().toString())
        .isEqualTo("[{AttributeName: " + hashKeyName + ",KeyType: HASH}]");
    assertThat(tableDesc.getAttributeDefinitions().toString())
        .isEqualTo("[{AttributeName: " + hashKeyName + ",AttributeType: S}]");
    assertThat(tableDesc.getProvisionedThroughput().getReadCapacityUnits()).isEqualTo(1000L);
    assertThat(tableDesc.getProvisionedThroughput().getWriteCapacityUnits()).isEqualTo(1000L);
    assertThat(tableDesc.getTableStatus()).isEqualTo("ACTIVE");
    assertThat(tableDesc.getTableArn())
        .isEqualTo("arn:aws:dynamodb:ddblocal:000000000000:table/order");
    assertThat(dynamoDB.listTables().getTableNames()).hasSizeGreaterThanOrEqualTo(1);
  }

  private CreateTableResult createTable(String tableName, String hashKeyName) {
    List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
    attributeDefinitions.add(new AttributeDefinition(hashKeyName, ScalarAttributeType.S));

    List<KeySchemaElement> ks = new ArrayList<>();
    ks.add(new KeySchemaElement(hashKeyName, KeyType.HASH));

    ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);

    CreateTableRequest request =
        new CreateTableRequest()
            .withTableName(tableName)
            .withAttributeDefinitions(attributeDefinitions)
            .withKeySchema(ks)
            .withProvisionedThroughput(provisionedthroughput);

    return dynamoDB.createTable(request);
  }
}
