package controller;

import dao.BookingDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import model.Booking;
import service.MongoLogService;

public class BookingController {
    private final BookingDAO bookingDAO;

    public BookingController(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public boolean buatBooking(int meja, int pelangganId, String waktuStr) {
        try {
            LocalDateTime waktu = LocalDateTime.parse(waktuStr);
            if (bookingDAO.isMejaAvailable(meja, waktu)) {
                Booking booking = new Booking(meja, pelangganId, waktu);
                bookingDAO.insert(booking);
                MongoLogService.log("New booking created for table " + meja + " at " + waktu);
                return true;
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date time format: " + waktuStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBooking(String id, int meja, int pelangganId, String waktuStr) {
        try {
            LocalDateTime waktu = LocalDateTime.parse(waktuStr);
            Booking booking = new Booking(id, meja, pelangganId, waktu);
            bookingDAO.update(booking);
            MongoLogService.log("Booking " + id + " updated.");
            return true;
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date time format: " + waktuStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete
    public void deleteBooking(String id) {
        try {
            bookingDAO.delete(id);
            MongoLogService.log("Booking " + id + " deleted.");
        } catch (Exception e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.getAll();
    }
}