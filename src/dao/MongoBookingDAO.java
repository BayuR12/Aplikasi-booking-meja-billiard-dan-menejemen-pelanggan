package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.Booking;
import org.bson.Document;
import util.MongoUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoBookingDAO implements BookingDAO {

    private final MongoCollection<Document> collection = MongoUtil.getDatabase().getCollection("booking");

    @Override
    public void insert(Booking b) {
        Document doc = new Document("meja", b.getMeja())
                .append("pelangganId", b.getPelangganId()) // Sebaiknya ini merujuk ke ObjectId pelanggan
                .append("waktu", Date.from(b.getWaktu().atZone(ZoneId.systemDefault()).toInstant()));
        collection.insertOne(doc);
    }

    @Override
    public void update(Booking b) {
        // Dibiarkan kosong sesuai implementasi MySQL sebelumnya
    }

    @Override
    public void delete(int id) {
        // Dibiarkan kosong sesuai implementasi MySQL sebelumnya
    }

    @Override
    public Booking getById(int id) {
        // Dibiarkan kosong sesuai implementasi MySQL sebelumnya
        return null;
    }

    @Override
    public List<Booking> getAll() {
        // Dibiarkan kosong sesuai implementasi MySQL sebelumnya
        return new ArrayList<>();
    }

    @Override
    public boolean isMejaAvailable(int meja, LocalDateTime waktu) {
        Date waktuDate = Date.from(waktu.atZone(ZoneId.systemDefault()).toInstant());
        long count = collection.countDocuments(
                Filters.and(
                        Filters.eq("meja", meja),
                        Filters.eq("waktu", waktuDate)
                )
        );
        return count == 0;
    }
}