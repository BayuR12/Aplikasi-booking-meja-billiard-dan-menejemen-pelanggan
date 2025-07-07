package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.bson.Document;
import util.MongoUtil;

public class MongoLogService {

    public static void log(String message) {
        try {
            MongoDatabase db = MongoUtil.getDatabase();
            MongoCollection<Document> logCollection = db.getCollection("logs");
            
            Document logDoc = new Document()
                    .append("timestamp", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                    .append("message", message);
            
            logCollection.insertOne(logDoc);
        } catch (Exception e) {
            System.err.println("Failed to write log to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}