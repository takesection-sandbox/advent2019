package com.pigumer;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;

@ApplicationScoped
public class ProcessingService {
    private String stage = System.getenv("STAGE");
    private String endpoint = System.getenv("ENDPOINT");
    private String port = System.getenv("PORT");
    private String redis = System.getenv("ENABLED_REDIS");

    /*
    private OutputObject redis(InputObject input) {
        RedisClient client = RedisClient.create("redis://" + endpoint + ":" + port);
        try (StatefulRedisConnection<String, String> conn = client.connect()) {
            RedisCommands<String, String> cmd = conn.sync();
            cmd.set(input.getName(), input.getGreeting());
            OutputObject res = new OutputObject();
            res.setResult(cmd.get(input.getName()));
            return res;
        } finally {
            client.shutdown();
        }
    }
    */

    private OutputObject dynamoDB(InputObject input) {
        Regions region = Regions.fromName(System.getenv("AWS_REGION"));
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("id", new AttributeValue(input.getName()));

        HashMap<String, AttributeValueUpdate> values = new HashMap<>();
        values.put("greeting", new AttributeValueUpdate(new AttributeValue(input.getGreeting()), AttributeAction.PUT));

        client.updateItem("advent2019-" + stage, itemKey, values);

        return new OutputObject().setResult(input.getName());
    }

    public OutputObject proces(InputObject input) {
        //if (redis == null || !Boolean.valueOf(redis)) {
            return dynamoDB(input);
        //}
        //return redis(input);
    }
}
