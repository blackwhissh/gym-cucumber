package com.epam.trainingservice.service.aws;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.*;
import com.epam.trainingservice.entity.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamoDBService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DynamoDBService.class);
    private final AmazonDynamoDB dynamoDB;

    public DynamoDBService(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }
    public void updateDuration(String username, Summary summary) {
        LOGGER.info("Started update duration");
        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("trainer_username", new AttributeValue(username));

        HashMap<String, AttributeValueUpdate> updatedValue = new HashMap<>();
        updatedValue.put("years", new AttributeValueUpdate((AttributeValue) summary.getYears(), AttributeAction.PUT));

        UpdateItemRequest request = new UpdateItemRequest(
                "trainer_info",
                itemKey,
                updatedValue
        );

        try {
            dynamoDB.updateItem(request);
            LOGGER.info("Item updated successfully");
        } catch (ResourceNotFoundException e) {
            LOGGER.warn("Resource not found");
            LOGGER.error(e.getMessage());
        }
    }

    public Summary findByUsername(String username) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDB);
        LOGGER.info("Started find item by username");
        try {
            Summary partitionKey = new Summary();
            partitionKey.setTrainerUsername(username);
            DynamoDBQueryExpression<Summary> queryExpression = new DynamoDBQueryExpression<Summary>()
                    .withHashKeyValues(partitionKey);
            Summary summary = dynamoDBMapper.query(Summary.class, queryExpression).stream().findFirst().orElseThrow();
            LOGGER.info("Item found successfully");
            return summary;
        } catch (ResourceNotFoundException e) {
            LOGGER.warn("Resource not found");
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public void save(Summary summary) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDB);
        LOGGER.info("Started save item");
        dynamoDBMapper.save(summary);
        LOGGER.info("Item saved successfully");
    }

    public void listTables() {
        ListTablesRequest request;
        boolean more_tables = true;
        String last_name = null;
        while(more_tables) {
            try {
                if (last_name == null) {
                    request = new ListTablesRequest().withLimit(10);
                } else {
                    request = new ListTablesRequest()
                            .withLimit(10)
                            .withExclusiveStartTableName(last_name);
                }
                ListTablesResult table_list = dynamoDB.listTables(request);
                List<String> table_names = table_list.getTableNames();
                if (!table_names.isEmpty()) {
                    for (String cur_name : table_names) {
                        System.out.format("* %s\n", cur_name);
                    }
                } else {
                    System.out.println("No tables found!");
                    System.exit(0);
                }
                last_name = table_list.getLastEvaluatedTableName();
                if (last_name == null) {
                    more_tables = false;
                }
            } catch (RuntimeException e){
                throw new RuntimeException(e);
            }
        }
    }


}
