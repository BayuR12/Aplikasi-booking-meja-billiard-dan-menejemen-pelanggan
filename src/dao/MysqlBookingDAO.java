package dao;

import model.Booking;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MysqlBookingDAO implements BookingDAO {
    @Override
    public void insert(Booking b) {
        String sql = "INSERT INTO booking (meja, pelanggan_id, waktu) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getMeja());
            stmt.setInt(2, b.getPelangganId());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getWaktu()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Booking b) {}

    @Override
    public void delete(int id) {}

    @Override
    public Booking getById(int id) { return null; }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>();
    }

    @Override
    public boolean isMejaAvailable(int meja, LocalDateTime waktu) {
        String sql = "SELECT COUNT(*) FROM booking WHERE meja = ? AND waktu = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meja);
            stmt.setTimestamp(2, Timestamp.valueOf(waktu));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}