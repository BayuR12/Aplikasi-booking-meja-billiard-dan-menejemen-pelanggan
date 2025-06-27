package dao;

import model.Pelanggan;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlPelangganDAO implements PelangganDAO {
    @Override
    public void insert(Pelanggan p) {
        // Kolom password ditambahkan ke query
        String sql = "INSERT INTO pelanggan (nama, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNama());
            stmt.setString(2, p.getEmail());
            stmt.setString(3, p.getPassword()); // Mengambil hash password
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Pelanggan p) {
        // Kolom password ditambahkan ke query
        String sql = "UPDATE pelanggan SET nama = ?, email = ?, password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNama());
            stmt.setString(2, p.getEmail());
            stmt.setString(3, p.getPassword());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM pelanggan WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pelanggan getById(int id) {
        String sql = "SELECT * FROM pelanggan WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pelanggan(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pelanggan> getAll() {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pelanggan(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Pelanggan getByEmail(String email) {
        String sql = "SELECT * FROM pelanggan WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pelanggan(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}