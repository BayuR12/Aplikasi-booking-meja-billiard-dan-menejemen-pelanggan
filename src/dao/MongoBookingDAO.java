package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.io.*;
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
    private final String bookingFile = "bookings_backup.dat";

    @Override
    public void insert(Booking b) {
        try {
            Document doc = new Document("meja", b.getMeja())
                    .append("pelangganId", b.getPelangganId())
                    .append("waktu", Date.from(b.getWaktu().atZone(ZoneId.systemDefault()).toInstant()));
            collection.insertOne(doc);
            System.out.println("✅ Berhasil menyimpan ke Database MongoDB.");
        } catch (Exception e) {
            System.err.println("❌ Gagal menyimpan ke MongoDB: " + e.getMessage());
            return;
        }

        System.out.println("Menyimpan backup ke File...");
        saveToFile(b);
    }

    private void saveToFile(Booking booking) {
        File file = new File(bookingFile);
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            if (file.length() == 0) {
                try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(booking);
                }
            } else {
                try (AppendingObjectOutputStream aoos = new AppendingObjectOutputStream(fos)) {
                    aoos.writeObject(booking);
                }
            }
            System.out.println("✅ Berhasil menyimpan backup ke File.");
        } catch (IOException e) {
            System.err.println("❌ Gagal menyimpan backup ke File: " + e.getMessage());
        }
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

    @Override
    public void delete(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            collection.deleteOne(Filters.eq("_id", objectId));
            System.out.println("Booking dengan ID " + id + " berhasil dihapus dari MongoDB.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: ID tidak valid. " + e.getMessage());
        }
    }

    private Booking docToBooking(Document doc) {
        if (doc == null) return null;
        
        LocalDateTime waktu = doc.getDate("waktu").toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
        
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
        for (Document doc : collection.find().sort(new Document("waktu", 1))) {
            bookingList.add(docToBooking(doc));
        }
        return bookingList;
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


class AppendingObjectOutputStream extends ObjectOutputStream {
    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset();
    }
}