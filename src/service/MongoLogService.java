// src/service/MongoLogService.java
package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.Document;
import util.MongoUtil;
import java.util.Date;
import java.util.List;

public class MongoLogService {
    private static final MongoDatabase db = MongoUtil.getDatabase();
    private static final MongoCollection<Document> logs = db.getCollection("logs");

    public static void log(String activity) {
        Document log = new Document("activity", activity)
                .append("timestamp", new Date());
        logs.insertOne(log);
        System.out.println("LOG: " + activity);
    }
    
    public static List<Document> getAllLogs() {
        List<Document> logList = new ArrayList<>();
        // Mengurutkan log dari yang terbaru
        for (Document doc : logs.find().sort(new Document("timestamp", -1))) { 
            logList.add(doc);
        }
        return logList;
    }
}