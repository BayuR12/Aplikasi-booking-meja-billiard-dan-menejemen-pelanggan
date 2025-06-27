package ui;

import controller.LoginController;
import dao.MongoPelangganDAO;
import javax.swing.*;
import java.awt.*;


public class LoginPanel extends JPanel {
    private final MainFrame mainFrame; // Referensi ke MainFrame
    private final LoginController loginController;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.loginController = new LoginController(new MongoPelangganDAO());

        setLayout(new GridLayout(3, 2, 5, 5)); // Menambahkan jarak antar komponen
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Menambahkan padding

        JLabel userLabel = new JLabel("Email:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            String email = userField.getText();
            String password = new String(passField.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email dan password tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loginSuccess = loginController.login(email, password);

            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, "Login Berhasil!");
                mainFrame.showPanel("booking"); // Pindah ke panel booking jika berhasil
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal. Cek kembali email dan password Anda.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(new JLabel()); add(loginBtn);
    }
}