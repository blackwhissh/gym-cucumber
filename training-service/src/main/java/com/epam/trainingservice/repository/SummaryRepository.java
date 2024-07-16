package com.epam.trainingservice.repository;

import com.epam.trainingservice.entity.Summary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Update.update;

@Repository
public class SummaryRepository {
    private final MongoTemplate mongoTemplate;

    public SummaryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void updateDuration(String username, Summary summary){
        mongoTemplate.updateFirst(query(where("trainerUsername").is(username)),
                update("years", summary.getYears()), Summary.class);
    }
    public Summary findByUsername(String username){
        return mongoTemplate.findOne(query(where("trainerUsername").is(username)), Summary.class);
    }

    public void save(Summary summary){
        mongoTemplate.insert(summary);
    }
}
