package service;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

public class MongoLogService {
    private static final MongoDatabase db = MongoClients.create().getDatabase("billiard");
    private static final MongoCollection<Document> logs = db.getCollection("logs");

    public static void log(String activity) {
        Document log = new Document("activity", activity).append("timestamp", new Date());
        logs.insertOne(log);
    }
}