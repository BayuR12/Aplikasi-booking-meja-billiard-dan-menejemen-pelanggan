package ui;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Billiard Booking");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Locale locale = new Locale("id"); // Bisa diganti en, id, dll
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", locale);

        JLabel welcomeLabel = new JLabel(bundle.getString("welcome"));
        add(welcomeLabel);
    }
}