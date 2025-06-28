package controller;

import dao.PelangganDAO;
import model.Pelanggan;

public class LoginController {
    private final PelangganDAO pelangganDAO;

    public LoginController(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public boolean login(String email, String password) {
        Pelanggan p = pelangganDAO.getByEmail(email);

        if (p != null) {
            return password.equals(p.getPassword());
        }
        return false;
    }
}
