package ui;

import javax.swing.*;
import java.awt.*;
import service.HashingUtil;

public class LoginPanel extends JPanel {
    public LoginPanel() {
        setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Email:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            String email = userField.getText();
            String password = new String(passField.getPassword());
            String hash = HashingUtil.sha256(password);
            JOptionPane.showMessageDialog(this, "Login attempt for: " + email + "\nHash: " + hash);
        });

        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(new JLabel()); add(loginBtn);
    }

    LoginPanel(MainFrame aThis) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}