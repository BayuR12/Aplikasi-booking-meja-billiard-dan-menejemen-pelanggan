package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Pelanggan;
import org.bson.Document;
import util.MongoUtil;

import java.util.ArrayList;
import java.util.List;

public class MongoPelangganDAO implements PelangganDAO {

    private final MongoCollection<Document> collection = MongoUtil.getDatabase().getCollection("pelanggan");

    // Helper untuk mengubah Document menjadi objek Pelanggan
    private Pelanggan docToPelanggan(Document doc) {
        if (doc == null) return null;
        // Di MongoDB, ID default adalah ObjectId. Kita simpan sebagai String di model.
        // Untuk konsistensi, kita tidak akan menggunakan ID integer.
        // Namun, jika ingin tetap pakai ID integer, perlu logika tambahan.
        return new Pelanggan(
                0, // ID integer tidak relevan di MongoDB, kita set 0
                doc.getString("nama"),
                doc.getString("email"),
                doc.getString("password")
        );
    }

    @Override
    public void insert(Pelanggan p) {
        Document doc = new Document("nama", p.getNama())
                .append("email", p.getEmail())
                .append("password", p.getPassword());
        collection.insertOne(doc);
    }

    @Override
    public void update(Pelanggan p) {
        // Asumsi update dilakukan berdasarkan email
        collection.updateOne(Filters.eq("email", p.getEmail()),
                Updates.combine(
                        Updates.set("nama", p.getNama()),
                        Updates.set("password", p.getPassword())
                ));
    }

    @Override
    public void delete(int id) {
        // Menghapus berdasarkan ID integer tidak praktis di MongoDB.
        // Sebaiknya ganti interface untuk delete by email atau ObjectId (String).
        // Untuk saat ini, kita biarkan kosong.
        System.out.println("Fungsi delete by integer ID tidak diimplementasikan untuk MongoDB.");
    }

    @Override
    public Pelanggan getById(int id) {
        // Sama seperti delete, get by integer ID tidak praktis.
        System.out.println("Fungsi get by integer ID tidak diimplementasikan untuk MongoDB.");
        return null;
    }

    @Override
    public List<Pelanggan> getAll() {
        List<Pelanggan> pelangganList = new ArrayList<>();
        for (Document doc : collection.find()) {
            pelangganList.add(docToPelanggan(doc));
        }
        return pelangganList;
    }

    @Override
    public Pelanggan getByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return docToPelanggan(doc);
    }
}