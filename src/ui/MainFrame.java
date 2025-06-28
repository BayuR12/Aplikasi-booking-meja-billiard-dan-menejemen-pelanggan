package ui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import service.BookingCheckerThread;

public final class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final BookingForm bookingForm;
    private final LanguageManager lang = LanguageManager.getInstance();

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu languageMenu = new JMenu();
    private final JMenuItem indonesianMenuItem = new JMenuItem();
    private final JMenuItem englishMenuItem = new JMenuItem();

    public MainFrame() {
        setTitle("Billiard Booking System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize panels
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        bookingForm = new BookingForm(this);

        // Add panels to the main panel with CardLayout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(bookingForm, "booking");

        add(mainPanel);

        setupMenu();
        
        // Start the background thread for checking bookings
        new BookingCheckerThread().start();
    }

    private void setupMenu() {
        indonesianMenuItem.addActionListener((ActionEvent e) -> {
            lang.setLocale(new Locale("in", "ID"));
            updateUI();
        });

        englishMenuItem.addActionListener((ActionEvent e) -> {
            lang.setLocale(new Locale("en", "US"));
            updateUI();
        });

        languageMenu.add(indonesianMenuItem);
        languageMenu.add(englishMenuItem);
        menuBar.add(languageMenu);
        setJMenuBar(menuBar);
        updateLanguageMenuItem(); // This call will now work correctly
    }
    
    /**
     * This method contains the fix.
     * It updates the text of the language menu items based on the current locale.
     */
    public void updateLanguageMenuItem() {
        // Implementasi yang benar, bukan "throw new UnsupportedOperationException"
        languageMenu.setText(lang.getString("language"));
        indonesianMenuItem.setText(lang.getString("indonesian"));
        englishMenuItem.setText(lang.getString("english"));
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    /**
     * Updates the texts on all panels when the language is changed.
     */
    private void updateUI() {
        loginPanel.updateTexts();
        registerPanel.updateTexts();
        bookingForm.updateTexts();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}