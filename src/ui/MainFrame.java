package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final LanguageManager lang = LanguageManager.getInstance();

    // Deklarasikan SEMUA komponen UI yang teksnya perlu diubah sebagai field
    private JLabel titleLabel;
    private JLabel languageLabel; // <-- Label "Bahasa:" ditambahkan sebagai field
    private JComboBox<String> languageComboBox;
    
    private LoginPanel loginPanel;
    private BookingForm bookingForm;
    private RegisterPanel registerPanel;

    public MainFrame() {
        // Panggil metode untuk membangun komponen UI
        initComponents();
    }

    private void initComponents() {
        // Inisialisasi komponen UI
        titleLabel = new JLabel("", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        languageLabel = new JLabel(); // Inisialisasi label bahasa
        
        // Panel Header dengan pilihan bahasa
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Dropdown bahasa
        JPanel languageSelectionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        languageSelectionPanel.add(languageLabel); // Tambahkan label ke panel
        
        String[] languages = {"Indonesia", "English"};
        languageComboBox = new JComboBox<>(languages);
        languageSelectionPanel.add(languageComboBox);
        
        headerPanel.add(languageSelectionPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Inisialisasi semua panel utama
        loginPanel = new LoginPanel(this);
        bookingForm = new BookingForm(this);
        registerPanel = new RegisterPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(bookingForm, "booking");
        mainPanel.add(registerPanel, "register");
        add(mainPanel, BorderLayout.CENTER);
        
        // Listener untuk JComboBox yang memanggil updateTexts() saat bahasa diubah
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

        // Pengaturan dasar untuk frame
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 450); // Sedikit diperlebar agar pas
        setLocationRelativeTo(null);
        
        // Panggil updateTexts() sekali saat startup untuk mengatur teks awal
        updateTexts();
    }
    
    private void updateTexts() {
        // Update teks di MainFrame
        setTitle(lang.getString("app.title"));
        titleLabel.setText(lang.getString("welcome"));
        languageLabel.setText(lang.getString("language") + ":"); // <-- Perbaikan ada di sini
        
        // Memerintahkan setiap panel anak untuk memperbarui teks mereka juga
        if (loginPanel != null) loginPanel.updateTexts();
        if (bookingForm != null) bookingForm.updateTexts();
        if (registerPanel != null) registerPanel.updateTexts();
    }

    
    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
}
