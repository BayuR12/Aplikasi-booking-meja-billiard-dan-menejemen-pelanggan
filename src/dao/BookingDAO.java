package dao;

import java.time.LocalDateTime;
import java.util.List;
import model.Booking;

public interface BookingDAO {
    void insert(Booking b);
    void update(Booking b); // Ditambahkan
    Booking get(int id);
    List<Booking> getAll(); // Ditambahkan
    void delete(int id);
    boolean isMejaAvailable(int tableNumber, LocalDateTime time);
}