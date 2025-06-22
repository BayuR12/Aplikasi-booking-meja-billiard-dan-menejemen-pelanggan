package dao;

import model.Pelanggan;

public interface PelangganDAO extends GenericDAO<Pelanggan> {
    Pelanggan getByEmail(String email);
}