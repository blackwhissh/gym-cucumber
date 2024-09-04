package com.epam.trainingservice.service.aws;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.entity.Summary;
import com.epam.trainingservice.exception.DynamoDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DynamoDBService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DynamoDBService.class);
    private final static String TABLE_NAME = "trainer_info";
    private final static String USERNAME = "trainer_username";
    private final static String STATUS = "trainee_status";
    private final static String FIRSTNAME = "trainer_first_name";
    private final static String LASTNAME = "trainer_last_name";
    private final AmazonDynamoDB dynamoDB;



    public DynamoDBService(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    private static List<AttributeValue> serializeMonthSummaries(List<TrainerSummary.MonthSummary> monthSummaries) {
        List<AttributeValue> monthSummaryList = new ArrayList<>();
        for (TrainerSummary.MonthSummary monthSummary : monthSummaries) {
            Map<String, AttributeValue> monthSummaryMap = new HashMap<>();
            monthSummaryMap.put("month", new AttributeValue().withS(String.valueOf(monthSummary.getMonth())));
            monthSummaryMap.put("summary", new AttributeValue().withN(String.valueOf(monthSummary.getDuration())));

            monthSummaryList.add(new AttributeValue().withM(monthSummaryMap));
        }
        return monthSummaryList;
    }

    private static List<AttributeValue> serializeYearSummaries(List<TrainerSummary.YearSummary> yearSummaries) {
        List<AttributeValue> yearSummaryList = new ArrayList<>();
        for (TrainerSummary.YearSummary yearSummary : yearSummaries) {
            Map<String, AttributeValue> yearSummaryMap = new HashMap<>();
            for (Integer key : yearSummary.getMonthsSummary().keySet()) {
                yearSummaryMap.put(""+key, new AttributeValue().withL(serializeMonthSummaries(yearSummary.getMonthsSummary().get(key))));
            }

            yearSummaryMap.put("months", new AttributeValue().withL());

            yearSummaryList.add(new AttributeValue().withM(yearSummaryMap));
        }
        return yearSummaryList;
    }

    private static List<TrainerSummary.MonthSummary> deserializeMonthSummaries(List<AttributeValue> monthSummaryList) {
        List<TrainerSummary.MonthSummary> monthSummaries = new ArrayList<>();
        for (AttributeValue av : monthSummaryList) {
            Map<String, AttributeValue> monthSummaryMap = av.getM();
            String month = monthSummaryMap.get("month").getS();
            String summary = monthSummaryMap.get("summary").getS();
            monthSummaries.add(new TrainerSummary.MonthSummary(Integer.parseInt(month), Integer.parseInt(summary)));
        }
        return monthSummaries;
    }

    private static List<TrainerSummary.YearSummary> deserializeYearSummaries(List<AttributeValue> yearSummaryList) {
        List<TrainerSummary.YearSummary> yearSummaries = new ArrayList<>();
        for (AttributeValue av : yearSummaryList) {
            Map<String, AttributeValue> yearSummaryMap = av.getM();
            int year = Integer.parseInt(yearSummaryMap.get("year").getN());
            List<TrainerSummary.MonthSummary> months = deserializeMonthSummaries(yearSummaryMap.get("months").getL());
            HashMap<Integer, List<TrainerSummary.MonthSummary>> map = new HashMap<>();
            map.put(year, months);
            yearSummaries.add(new TrainerSummary.YearSummary(map));
        }
        return yearSummaries;
    }

    public void updateDuration(String username, Summary summary) {
        LOGGER.info("Started update duration");
        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put(USERNAME, new AttributeValue(username));

        HashMap<String, AttributeValueUpdate> updatedValue = new HashMap<>();
        updatedValue.put("years", new AttributeValueUpdate((AttributeValue) summary.getYears(), AttributeAction.PUT));

        UpdateItemRequest request = new UpdateItemRequest(
                TABLE_NAME,
                itemKey,
                updatedValue
        );

        try {
            dynamoDB.updateItem(request);
            LOGGER.info("Item updated successfully");
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Resource not found: " + e.getMessage());
            throw new DynamoDBException();
        }
    }

    public Optional<Summary> findByUsername(String username, String status) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(USERNAME, new AttributeValue().withS(username));
        key.put(STATUS, new AttributeValue().withS(status));

        GetItemRequest request = new GetItemRequest()
                .withTableName(TABLE_NAME)
                .withKey(key);

        LOGGER.info("Started find item by username");
        try {
            GetItemResult result = dynamoDB.getItem(request);
            if (!result.getItem().isEmpty()) {
                Map<String, AttributeValue> item = result.getItem();
                List<AttributeValue> yearSummaryList = item.get("years").getL();
                List<TrainerSummary.YearSummary> years = deserializeYearSummaries(yearSummaryList);
                return Optional.of(new Summary(
                        item.get(USERNAME).getS(),
                        item.get(FIRSTNAME).getS(),
                        item.get(LASTNAME).getS(),
                        item.get(STATUS).getS(),
                        years));
            }
        } catch (Exception e) {
            LOGGER.error("Item not found: " + e.getMessage());
            throw new DynamoDBException();
        }
        return Optional.empty();
    }

    public void save(Summary summary) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(USERNAME, new AttributeValue().withS(summary.getTrainerUsername()));
        item.put(STATUS, new AttributeValue().withS(summary.getTrainerStatus()));
        item.put(FIRSTNAME, new AttributeValue().withS(summary.getTrainerFirstName()));
        item.put(LASTNAME, new AttributeValue().withS(summary.getTrainerLastName()));
        item.put("years", new AttributeValue().withL(serializeYearSummaries(summary.getYears())));

        PutItemRequest request = new PutItemRequest()
                .withTableName(TABLE_NAME)
                .withItem(item);

        try {
            dynamoDB.putItem(request);
            LOGGER.info("Item added successfully!");
        } catch (Exception e) {
            LOGGER.error("Failed to create item in DynamoDB table: " + e.getMessage());
            throw new DynamoDBException();
        }
    }

    public void listTables() {
        LOGGER.info("Started list tables method");
        ListTablesRequest request;
        boolean moreTables = true;
        String lastName = null;
        while (moreTables) {
            try {
                if (lastName == null) {
                    request = new ListTablesRequest().withLimit(10);
                } else {
                    request = new ListTablesRequest()
                            .withLimit(10)
                            .withExclusiveStartTableName(lastName);
                }
                ListTablesResult tableList = dynamoDB.listTables(request);
                List<String> tableNames = tableList.getTableNames();
                if (!tableNames.isEmpty()) {
                    for (String currentName : tableNames) {
                        LOGGER.info("* " + currentName);
                    }
                } else {
                    LOGGER.warn("No tables found!");
                }
                lastName = tableList.getLastEvaluatedTableName();
                if (lastName == null) {
                    moreTables = false;
                }
            } catch (RuntimeException e) {
                LOGGER.error("Exception occurred during listing tables: " + e.getMessage());
                throw new DynamoDBException();
            }
        }
    }


}
