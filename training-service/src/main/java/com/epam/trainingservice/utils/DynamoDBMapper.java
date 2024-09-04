package com.epam.trainingservice.utils;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.entity.Summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBMapper {
    public static Summary itemToSummary(Item item) {
        if (item == null) {
            return null;
        }

        String trainerUsername = item.getString("trainerUsername");
        String trainerFirstName = item.getString("trainerFirstName");
        String trainerLastName = item.getString("trainerLastName");
        String trainerStatus = item.getString("trainerStatus");

        // Assuming 'years' is stored as a List of Maps
        List<Map<String, Object>> yearsData = item.getList("years");
        List<TrainerSummary.YearSummary> years = new ArrayList<>();
        if (yearsData != null) {
            for (Map<String, Object> yearData : yearsData) {
                String yearKey = yearData.keySet().iterator().next();
                List<Map<String, Object>> monthsData = (List<Map<String, Object>>) yearData.get(yearKey);
                HashMap<Integer, List<TrainerSummary.MonthSummary>> monthSummaryMap = new HashMap<>();
                List<TrainerSummary.MonthSummary> monthSummaries = new ArrayList<>();
                if (monthsData != null) {
                    for (Map<String, Object> monthData : monthsData) {
                        int month = (Integer) monthData.get("month");
                        int duration = (Integer) monthData.get("duration");
                        monthSummaries.add(new TrainerSummary.MonthSummary(month, duration));
                    }
                }
                monthSummaryMap.put(Integer.parseInt(yearKey), monthSummaries);
                years.add(new TrainerSummary.YearSummary(monthSummaryMap));
            }
        }

        return new Summary(trainerUsername, trainerFirstName, trainerLastName, trainerStatus, years);

    }
}
