package ui;

import controller.PelangganController;
import dao.MongoPelangganDAO;
import model.Pelanggan;
import service.MongoLogService; // <-- Tambahkan impor ini
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public final class RegisterPanel extends JPanel {
    private final MainFrame mainFrame;
    private final PelangganController pelangganController;
    private final LanguageManager lang = LanguageManager.getInstance();

    private final JLabel nameLabel = new JLabel();
    private final JLabel emailLabel = new JLabel();
    private final JLabel passLabel = new JLabel();
    private final JLabel confirmPassLabel = new JLabel();
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);
    private final JPasswordField confirmPassField = new JPasswordField(20);
    private final JButton registerBtn = new JButton();
    private final JButton backBtn = new JButton();

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.pelangganController = new PelangganController(new MongoPelangganDAO());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(confirmPassLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(confirmPassField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        backBtn.addActionListener(e -> mainFrame.showPanel("login"));
        registerBtn.addActionListener(e -> {
            String nama = nameField.getText();
            String email = emailField.getText();
            char[] password = passField.getPassword();
            char[] confirmPassword = confirmPassField.getPassword();

            if (nama.isEmpty() || email.isEmpty() || password.length == 0) {
                JOptionPane.showMessageDialog(this, lang.getString("emptyFields"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Arrays.equals(password, confirmPassword)) {
                JOptionPane.showMessageDialog(this, lang.getString("passwordMismatch"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pelanggan newPelanggan = new Pelanggan(0, nama, email, new String(password));
            pelangganController.tambahPelanggan(newPelanggan);
            
            MongoLogService.log("New user registered successfully: " + email); // <-- Tambahkan log

            JOptionPane.showMessageDialog(this, lang.getString("registrationSuccess"), lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showPanel("login");
        });
        
        updateTexts();
    }
    
    public void updateTexts() {
        nameLabel.setText(lang.getString("name"));
        emailLabel.setText(lang.getString("email"));
        passLabel.setText(lang.getString("password"));
        confirmPassLabel.setText(lang.getString("confirmPassword"));
        registerBtn.setText(lang.getString("register"));
        backBtn.setText(lang.getString("back"));
        mainFrame.updateLanguageMenuItem();
    }
}