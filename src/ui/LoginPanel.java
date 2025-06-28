package ui;

import controller.LoginController;
import dao.MongoPelangganDAO;
import service.MongoLogService; // <-- Tambahkan impor ini

import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

public final class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private final LoginController loginController;
    private final LanguageManager lang = LanguageManager.getInstance();

    private final JLabel userLabel = new JLabel();
    private final JLabel passLabel = new JLabel();
    private final JTextField userField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);
    private final JButton loginBtn = new JButton();
    private final JButton registerBtn = new JButton();

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.loginController = new LoginController(new MongoPelangganDAO());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        registerBtn.addActionListener(e -> mainFrame.showPanel("register"));
        loginBtn.addActionListener(e -> {
            String email = userField.getText();
            String password = new String(passField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, lang.getString("emptyFields"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loginSuccess = loginController.login(email, password);

            if (loginSuccess) {
                MongoLogService.log("User logged in successfully: " + email); // <-- Tambahkan log
                JOptionPane.showMessageDialog(this, lang.getString("loginSuccess"), lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showPanel("booking");
            } else {
                MongoLogService.log("Failed login attempt for email: " + email); // <-- Tambahkan log
                JOptionPane.showMessageDialog(this, lang.getString("loginFailed"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        updateTexts();
    }

    public void updateTexts() {
        userLabel.setText(lang.getString("email"));
        passLabel.setText(lang.getString("password"));
        loginBtn.setText(lang.getString("login"));
        registerBtn.setText(lang.getString("register"));
        mainFrame.updateLanguageMenuItem();
    }
}