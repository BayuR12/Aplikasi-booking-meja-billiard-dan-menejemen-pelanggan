package controller;

import model.Pelanggan;
import dao.PelangganDAO;
import service.HashingUtil;

public class LoginController {
    private PelangganDAO pelangganDAO;

    public LoginController(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public boolean login(String email, String password) {
        Pelanggan p = pelangganDAO.getByEmail(email);
        if (p != null) {
            String hashedPassword = HashingUtil.sha256(password);
            // Simplifikasi: anggap password yang disimpan adalah hash dari email
            return hashedPassword.equals(HashingUtil.sha256(p.getEmail()));
        }
        return false;
    }
}