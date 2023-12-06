package com.dailyon.orderservice.domain.torder.entity;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dailyon.orderservice.ContainerBaseTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TOrderTest extends ContainerBaseTestSupport {

  @Autowired private AmazonDynamoDB dynamoDB;

  @AfterEach
  void after() {

    DeleteTableRequest deleteTableRequest = new DeleteTableRequest("orders");
    TableUtils.deleteTableIfExists(dynamoDB, deleteTableRequest);
  }

  @Test
  @DisplayName("dynamodb order table 생성 테스트")
  void test_createTable() throws Exception {
    // given
    String tableName = "orders";
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

    // ThrottlingException 을 반환하기 전 초당 사용 되는 최대 쓰기, 일기 수 PAY_PER_REQUEST 일 경우 0으로 설정된다.
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
