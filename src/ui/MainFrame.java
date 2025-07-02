package ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import service.BookingCheckerThread;
import util.MongoUtil;

public final class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private BookingForm bookingForm;
    private LogPanel logPanel;
    private final LanguageManager lang = LanguageManager.getInstance();
    private BookingCheckerThread bookingCheckerThread;

    // Menu Bar
    private JMenuBar menuBar;
    private JMenu fileMenu, viewMenu, languageMenu;
    private JMenuItem exitMenuItem, viewLogsMenuItem, indonesianMenuItem, englishMenuItem;

    public MainFrame() {
        initComponents();
        
        // Hapus baris ini agar jendela tidak langsung maximized
        // setExtendedState(JFrame.MAXIMIZED_BOTH); 

        bookingCheckerThread = new BookingCheckerThread();
        bookingCheckerThread.start();

        // Mengatur listener untuk event penutupan jendela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Logika untuk menutup resource dengan aman
                bookingCheckerThread.stopThread();
                MongoUtil.close();
                System.exit(0); // Keluar dari aplikasi
            }
        });
    }

    private void initComponents() {
        // Atur agar listener yang menangani penutupan, bukan aksi default
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        
        // Atur ukuran jendela default agar tidak layar penuh
        setSize(1280, 720);
        setMinimumSize(new Dimension(800, 600)); // Ukuran minimum agar UI tidak rusak
        setLocationRelativeTo(null); // Posisi jendela di tengah layar

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi semua panel
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        bookingForm = new BookingForm(this);
        logPanel = new LogPanel(this);

        // Menambahkan panel ke CardLayout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(bookingForm, "booking");
        mainPanel.add(logPanel, "logs");

        // Menambahkan Menu Bar
        setupMenuBar();

        add(mainPanel);
        updateTexts();
    }

    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu();
        exitMenuItem = new JMenuItem();
        fileMenu.add(exitMenuItem);

        viewMenu = new JMenu();
        viewLogsMenuItem = new JMenuItem();
        viewMenu.add(viewLogsMenuItem);

        languageMenu = new JMenu();
        indonesianMenuItem = new JMenuItem("Indonesia");
        englishMenuItem = new JMenuItem("English");
        languageMenu.add(indonesianMenuItem);
        languageMenu.add(englishMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(languageMenu);
        setJMenuBar(menuBar);

        // Action Listeners for Menu
        exitMenuItem.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        viewLogsMenuItem.addActionListener(e -> showPanel("logs"));
        indonesianMenuItem.addActionListener(e -> changeLanguage("id", "ID"));
        englishMenuItem.addActionListener(e -> changeLanguage("en", "US"));
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
    
    public void changeLanguage(String langCode, String countryCode) {
        lang.setLanguage(langCode, countryCode);
        updateTexts();
    }
    
    private void updateTexts() {
        setTitle(lang.getString("app.title"));
        fileMenu.setText(lang.getString("fileMenu"));
        exitMenuItem.setText(lang.getString("exitMenu"));
        viewMenu.setText(lang.getString("viewMenu"));
        viewLogsMenuItem.setText(lang.getString("viewLogsMenu"));
        languageMenu.setText(lang.getString("languageMenu"));
        
        if(loginPanel != null) loginPanel.updateTexts();
        if(registerPanel != null) registerPanel.updateTexts();
        if(bookingForm != null) bookingForm.updateTexts();
        if(logPanel != null) logPanel.updateTexts();

        SwingUtilities.updateComponentTreeUI(this);
        revalidate();
        repaint();
    }
}
