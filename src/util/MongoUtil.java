package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "billiard";
    private static MongoClient mongoClient;

    static {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        } catch (Exception e) {
            System.err.println("Gagal terhubung ke MongoDB.");
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Koneksi MongoDB ditutup.");
        }
    }
}
