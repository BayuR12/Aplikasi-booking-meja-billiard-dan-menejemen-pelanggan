package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Booking;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.MongoUtil;

public class MongoBookingDAO implements BookingDAO {

    private final MongoDatabase db = MongoUtil.getDatabase();
    private final MongoCollection<Document> collection = db.getCollection("bookings");

    @Override
    public void insert(Booking b) {
        Document doc = new Document("meja", b.getMeja())
                .append("pelangganId", b.getPelangganId())
                .append("waktu", Date.from(b.getWaktu().atZone(ZoneId.systemDefault()).toInstant()));
        collection.insertOne(doc);
    }
    
    @Override
    public void update(Booking b) {
        ObjectId objectId = new ObjectId(b.getId());
        collection.updateOne(Filters.eq("_id", objectId),
                Updates.combine(
                        Updates.set("meja", b.getMeja()),
                        Updates.set("pelangganId", b.getPelangganId()),
                        Updates.set("waktu", Date.from(b.getWaktu().atZone(ZoneId.systemDefault()).toInstant()))
                ));
    }

    private Booking docToBooking(Document doc) {
        if (doc == null) return null;
        
        LocalDateTime waktu = doc.getDate("waktu").toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
        
        // Menggunakan ObjectId sebagai ID unik
        return new Booking(
            doc.getObjectId("_id").toHexString(), 
            doc.getInteger("meja"), 
            doc.getInteger("pelangganId"), 
            waktu
        );
    }
    
    @Override
    public List<Booking> getAll() {
        List<Booking> bookingList = new ArrayList<>();
        for (Document doc : collection.find().sort(new Document("waktu", 1))) { // Diurutkan berdasarkan waktu
            bookingList.add(docToBooking(doc));
        }
        return bookingList;
    }
    
    @Override
    public Booking get(int id) {
        // Implementasi ini mungkin perlu diubah karena ID sekarang String
        // Untuk saat ini, kita biarkan tidak terpakai
        return null;
    }

    @Override
    public void delete(int id) {
        // Implementasi ini juga perlu disesuaikan untuk ObjectId
    }

    @Override
    public boolean isMejaAvailable(int tableNumber, LocalDateTime time) {
        LocalDateTime oneHourBefore = time.minusHours(1);
        LocalDateTime oneHourAfter = time.plusHours(1);

        Document query = new Document("meja", tableNumber)
                .append("waktu", new Document("$gte", Date.from(oneHourBefore.atZone(ZoneId.systemDefault()).toInstant()))
                                .append("$lt", Date.from(oneHourAfter.atZone(ZoneId.systemDefault()).toInstant())));
        
        return collection.find(query).first() == null;
    }
}