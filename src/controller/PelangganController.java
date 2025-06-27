package controller;

import model.Pelanggan;
import dao.PelangganDAO;

import java.util.List;

public class PelangganController {
    private PelangganDAO pelangganDAO;

    public PelangganController(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public void tambahPelanggan(Pelanggan p) {
        pelangganDAO.insert(p);
    }

    public List<Pelanggan> getSemuaPelanggan() {
        return pelangganDAO.getAll();
    }

    public Pelanggan cariByEmail(String email) {
        return pelangganDAO.getByEmail(email);
    }
}