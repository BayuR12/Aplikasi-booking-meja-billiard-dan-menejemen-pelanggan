package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Locale currentLocale = new Locale("id");
    private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);

    public MainFrame() {
        setTitle(bundle.getString("welcome")); // Menggunakan kunci "welcome" yang ada
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Header
        JLabel title = new JLabel(bundle.getString("welcome"), JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Panels
        LoginPanel loginPanel = new LoginPanel(this); // Memanggil constructor yang benar
        BookingForm bookingForm = new BookingForm();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(bookingForm, "booking");
        add(mainPanel, BorderLayout.CENTER);

        // Footer Navigation
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton toLogin = new JButton("Login");
        JButton toBooking = new JButton("Booking");

        toLogin.addActionListener(e -> showPanel("login"));
        toBooking.addActionListener(e -> showPanel("booking"));

        footer.add(toLogin);
        footer.add(toBooking);
        add(footer, BorderLayout.SOUTH);
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
}