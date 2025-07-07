package dao;

import java.time.LocalDateTime;
import java.util.List;
import model.Booking;

public interface BookingDAO {
    void insert(Booking b);
    void update(Booking b);
    List<Booking> getAll();
    
    void delete(String id); 
    
    boolean isMejaAvailable(int tableNumber, LocalDateTime time);
}