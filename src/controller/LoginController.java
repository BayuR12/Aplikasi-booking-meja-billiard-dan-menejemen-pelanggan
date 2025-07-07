package controller;

import dao.PelangganDAO;
import model.Pelanggan;
import service.MongoLogService; 
import util.PasswordUtil;

public class LoginController {
    private final PelangganDAO pelangganDAO;

    public LoginController(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public boolean login(String email, char[] password) {
        Pelanggan pelanggan = pelangganDAO.getByEmail(email);
        
        if (pelanggan != null) {
            String plainPassword = new String(password);
            if (PasswordUtil.checkPassword(plainPassword, pelanggan.getPassword())) {
                
                MongoLogService.log("User logged in: " + email); 
                // ---------------------------------
                
                return true;
            }
        }
        
        return false; 
    }
}