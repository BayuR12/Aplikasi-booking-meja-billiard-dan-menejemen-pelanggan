package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import util.MongoUtil; // Impor MongoUtil

import java.util.Date;

public class MongoLogService {
    // Gunakan koneksi dari MongoUtil, sama seperti DAO lainnya
    private static final MongoDatabase db = MongoUtil.getDatabase();
    private static final MongoCollection<Document> logs = db.getCollection("logs");

    public static void log(String activity) {
        Document log = new Document("activity", activity).append("timestamp", new Date());
        logs.insertOne(log);
        System.out.println("LOG: " + activity); // Tambahkan ini untuk debugging di konsol
    }
}