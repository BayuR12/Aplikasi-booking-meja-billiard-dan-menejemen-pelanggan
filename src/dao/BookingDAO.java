package dao;

import model.Booking;

public interface BookingDAO extends GenericDAO<Booking> {
    boolean isMejaAvailable(int meja, java.time.LocalDateTime waktu);
}