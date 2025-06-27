package controller;

import model.Pelanggan;
import dao.PelangganDAO;
import service.HashingUtil;

public class LoginController {
    private final PelangganDAO pelangganDAO;

    public LoginController(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public boolean login(String email, String password) {
        Pelanggan p = pelangganDAO.getByEmail(email);
        if (p != null) {
            String hashedPassword = HashingUtil.sha256(password);
            // Membandingkan hash dari password input dengan hash yang tersimpan di DB
            return hashedPassword.equals(p.getPassword());
        }
        return false;
    }
}