// File: src/ui/MainFrame.java

package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private BookingForm bookingForm;
    private RegisterPanel registerPanel;
    private final LanguageManager lang = LanguageManager.getInstance();
    private JLabel titleLabel, languageLabel;
    private JComboBox<String> languageComboBox;

    public MainFrame() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginPanel = new LoginPanel(this);
        bookingForm = new BookingForm(this);
        registerPanel = new RegisterPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(bookingForm, "booking");
        mainPanel.add(registerPanel, "register");

        add(mainPanel);

        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        languageLabel = new JLabel();
        String[] languages = {"Indonesia", "English"};
        languageComboBox = new JComboBox<>(languages);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        languagePanel.add(languageLabel);
        languagePanel.add(languageComboBox);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(languagePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        languageComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedLanguage = (String) e.getItem();
                if (selectedLanguage.equals("English")) {
                    lang.setLanguage("en", "US");
                } else {
                    lang.setLanguage("id", "ID");
                }
                // Panggil metode untuk memperbarui SEMUA teks di aplikasi
                updateTexts();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        
        // Panggil updateTexts() sekali saat startup untuk mengatur teks awal
        updateTexts();
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    private void updateTexts() {
        // Update teks di MainFrame
        setTitle(lang.getString("app.title"));
        titleLabel.setText(lang.getString("welcome"));
        languageLabel.setText(lang.getString("language") + ":");

        // Memerintahkan setiap panel anak untuk memperbarui teks mereka juga
        if (loginPanel != null) loginPanel.updateTexts();
        if (bookingForm != null) bookingForm.updateTexts();
        if (registerPanel != null) registerPanel.updateTexts();

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}