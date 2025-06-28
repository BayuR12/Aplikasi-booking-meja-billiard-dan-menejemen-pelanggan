package ui;

import controller.LoginController;
import dao.MongoPelangganDAO;
import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

public final class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private final LoginController loginController;
    private final LanguageManager lang = LanguageManager.getInstance();

    // Deklarasikan komponen sebagai field
    private JLabel userLabel, passLabel;
    private JButton registerBtn, loginBtn;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.loginController = new LoginController(new MongoPelangganDAO());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        userLabel = new JLabel();
        JTextField userField = new JTextField();
        passLabel = new JLabel();
        JPasswordField passField = new JPasswordField();
        
        fieldsPanel.add(userLabel);
        fieldsPanel.add(userField);
        fieldsPanel.add(passLabel);
        fieldsPanel.add(passField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registerBtn = new JButton();
        loginBtn = new JButton();
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(loginBtn);
        
        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
                JOptionPane.showMessageDialog(this, lang.getString("loginSuccess"), lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showPanel("booking");
            } else {
                JOptionPane.showMessageDialog(this, lang.getString("loginFailed"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        updateTexts(); // Atur teks awal
    }

    public void updateTexts() {
        userLabel.setText(lang.getString("email"));
        passLabel.setText(lang.getString("password"));
        registerBtn.setText(lang.getString("register"));
        loginBtn.setText(lang.getString("login"));
    }
}
