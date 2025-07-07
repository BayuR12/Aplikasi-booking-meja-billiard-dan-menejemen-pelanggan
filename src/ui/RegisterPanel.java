package ui;

import controller.PelangganController;
import dao.MongoPelangganDAO;
import model.Pelanggan;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import util.PasswordUtil;

public final class RegisterPanel extends JPanel {
    private final MainFrame mainFrame;
    private final PelangganController pelangganController;
    private final LanguageManager lang = LanguageManager.getInstance();

    private JLabel nameLabel, emailLabel, passLabel, confirmPassLabel;
    private JButton registerBtn, backBtn;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.pelangganController = new PelangganController(new MongoPelangganDAO());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        nameLabel = new JLabel();
        JTextField nameField = new JTextField();
        emailLabel = new JLabel();
        JTextField emailField = new JTextField();
        passLabel = new JLabel();
        JPasswordField passField = new JPasswordField();
        confirmPassLabel = new JLabel();
        JPasswordField confirmPassField = new JPasswordField();

        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(emailField);
        fieldsPanel.add(passLabel);
        fieldsPanel.add(passField);
        fieldsPanel.add(confirmPassLabel);
        fieldsPanel.add(confirmPassField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backBtn = new JButton();
        registerBtn = new JButton();

        buttonPanel.add(backBtn);
        buttonPanel.add(registerBtn);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> mainFrame.showPanel("login"));
        registerBtn.addActionListener(e -> {
            String nama = nameField.getText();
            String email = emailField.getText();
            char[] password = passField.getPassword();
            char[] confirmPassword = confirmPassField.getPassword();

            if (nama.isEmpty() || email.isEmpty() || password.length == 0) {
                JOptionPane.showMessageDialog(this, lang.getString("allFieldsRequired"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Arrays.equals(password, confirmPassword)) {
                JOptionPane.showMessageDialog(this, lang.getString("passwordMismatch"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pelangganController.cariByEmail(email) != null) {
                JOptionPane.showMessageDialog(this, lang.getString("emailExists"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hashedPassword = PasswordUtil.hashPassword(new String(password));
            Pelanggan newPelanggan = new Pelanggan(0, nama, email, hashedPassword);
            pelangganController.tambahPelanggan(newPelanggan);

            JOptionPane.showMessageDialog(this, lang.getString("registrationSuccess"), lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showPanel("login");
        });
        
        updateTexts(); // Atur teks awal
    }

    public void updateTexts() {
        nameLabel.setText(lang.getString("fullName"));
        emailLabel.setText(lang.getString("email"));
        passLabel.setText(lang.getString("password"));
        confirmPassLabel.setText(lang.getString("confirmPassword"));
        backBtn.setText(lang.getString("backToLogin"));
        registerBtn.setText(lang.getString("registerAction"));
    }
}
