package controller;

import model.Booking;
import dao.BookingDAO;

import java.time.LocalDateTime;

public class BookingController {
    private BookingDAO bookingDAO;

    public BookingController(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public boolean buatBooking(int meja, int pelangganId, String waktuStr) {
        try {
            LocalDateTime waktu = LocalDateTime.parse(waktuStr);
            if (bookingDAO.isMejaAvailable(meja, waktu)) {
                Booking booking = new Booking(0, meja, pelangganId, waktu);
                bookingDAO.insert(booking);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}