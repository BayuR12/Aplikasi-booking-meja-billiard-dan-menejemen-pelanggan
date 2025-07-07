package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Date;
import org.bson.Document;
import util.MongoUtil;

public class MongoLogService {
    private static final MongoDatabase db = MongoUtil.getDatabase();
    private static final MongoCollection<Document> logs = db.getCollection("logs");

    public static void log(String activity) {
        System.out.println("LOGGING: " + activity);
        try {
            Document log = new Document("activity", activity)
                    .append("timestamp", new Date());
            logs.insertOne(log);
        } catch (Exception e) {
            System.err.println("Gagal menyimpan log ke database!");
            e.printStackTrace();
        }
    }
}