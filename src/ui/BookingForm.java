package ui;

import controller.BookingController;
import dao.MongoBookingDAO;
import model.Booking;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class BookingForm extends JPanel {
    private final BookingController bookingController;
    private final LanguageManager lang = LanguageManager.getInstance();
    private final MainFrame mainFrame;

    // Komponen UI
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton addOrUpdateBtn, clearBtn, backToLoginBtn, refreshBtn;
    private JTextField mejaField, pelangganField, waktuField;
    private JLabel mejaLabel, pelangganLabel, waktuLabel;
    private String selectedBookingId = null;

    // Variabel untuk menyimpan referensi border agar mudah diubah
    private TitledBorder formTitleBorder;
    private TitledBorder tableTitleBorder;

    public BookingForm(MainFrame frame) {
        this.mainFrame = frame;
        this.bookingController = new BookingController(new MongoBookingDAO());
        
        setLayout(new BorderLayout(10, 10));
        
        // Panel Form Input
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        
        // Buat border dan simpan referensinya
        formTitleBorder = BorderFactory.createTitledBorder(lang.getString("bookingData"));
        formPanel.setBorder(BorderFactory.createCompoundBorder(formTitleBorder, BorderFactory.createEmptyBorder(10,10,10,10)));

        mejaLabel = new JLabel();
        mejaField = new JTextField();
        pelangganLabel = new JLabel();
        pelangganField = new JTextField();
        waktuLabel = new JLabel();
        waktuField = new JTextField();
        waktuField.setToolTipText("YYYY-MM-DDTHH:MM:SS, e.g., 2025-12-31T23:59:00");
        
        addOrUpdateBtn = new JButton();
        clearBtn = new JButton();

        formPanel.add(mejaLabel);
        formPanel.add(mejaField);
        formPanel.add(pelangganLabel);
        formPanel.add(pelangganField);
        formPanel.add(waktuLabel);
        formPanel.add(waktuField);
        formPanel.add(addOrUpdateBtn);
        formPanel.add(clearBtn);

        // Panel Tabel
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        // Buat border dan simpan referensinya
        tableTitleBorder = BorderFactory.createTitledBorder(lang.getString("bookingList"));
        tablePanel.setBorder(tableTitleBorder);

        String[] columnNames = {"ID", "Table", "Customer ID", "Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.getColumnModel().getColumn(0).setMinWidth(0);
        bookingTable.getColumnModel().getColumn(0).setMaxWidth(0);
        bookingTable.getColumnModel().getColumn(0).setWidth(0);

        tablePanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        
        // Panel Tombol Bawah
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton();
        backToLoginBtn = new JButton();
        bottomButtonPanel.add(refreshBtn);
        bottomButtonPanel.add(backToLoginBtn);

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomButtonPanel, BorderLayout.SOUTH);

        // Action Listeners
        backToLoginBtn.addActionListener(e -> mainFrame.showPanel("login"));
        refreshBtn.addActionListener(e -> {
            loadBookings();
            clearForm();
        });
        clearBtn.addActionListener(e -> clearForm());

        bookingTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && bookingTable.getSelectedRow() != -1) {
                populateFormFromTable();
            }
        });

        addOrUpdateBtn.addActionListener(e -> saveBooking());
        
        updateTexts();
        loadBookings();
    }

    private void populateFormFromTable() {
        int selectedRow = bookingTable.getSelectedRow();
        selectedBookingId = tableModel.getValueAt(selectedRow, 0).toString();
        mejaField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        pelangganField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        
        String timeFromTable = tableModel.getValueAt(selectedRow, 3).toString();
        try {
           LocalDateTime dateTime = LocalDateTime.parse(timeFromTable, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
           waktuField.setText(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (Exception e) {
            waktuField.setText(timeFromTable);
        }
        addOrUpdateBtn.setText(lang.getString("updateBooking"));
    }

    private void saveBooking() {
        try {
            int meja = Integer.parseInt(mejaField.getText());
            int pelangganId = Integer.parseInt(pelangganField.getText());
            String waktuStr = waktuField.getText();

            if (waktuStr.isEmpty() || mejaField.getText().isEmpty() || pelangganField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, lang.getString("allFieldsRequired"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDateTime.parse(waktuStr);

            boolean success;
            if (selectedBookingId != null) { // Mode Edit
                success = bookingController.updateBooking(selectedBookingId, meja, pelangganId, waktuStr);
            } else { // Mode Tambah
                success = bookingController.buatBooking(meja, pelangganId, waktuStr);
            }

            if (success) {
                String messageKey = (selectedBookingId != null) ? "bookingUpdateSuccess" : "bookingSuccess";
                JOptionPane.showMessageDialog(this, lang.getString(messageKey), lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
                loadBookings();
                clearForm();
            } else {
                String messageKey = (selectedBookingId != null) ? "bookingUpdateFailed" : "bookingFailed";
                JOptionPane.showMessageDialog(this, lang.getString(messageKey), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, lang.getString("invalidNumber"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, lang.getString("invalidTimeFormat") + "\nContoh: 2025-12-31T23:59:00", lang.getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingController.getAllBookings();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                b.getId(),
                b.getMeja(),
                b.getPelangganId(),
                b.getWaktu().format(formatter)
            });
        }
    }

    private void clearForm() {
        selectedBookingId = null;
        mejaField.setText("");
        pelangganField.setText("");
        waktuField.setText("");
        bookingTable.clearSelection();
        addOrUpdateBtn.setText(lang.getString("addBooking"));
    }
    
    public void updateTexts(){
        mejaLabel.setText(lang.getString("tableNumber"));
        pelangganLabel.setText(lang.getString("customerId"));
        waktuLabel.setText(lang.getString("bookingTime"));
        addOrUpdateBtn.setText(lang.getString("addBooking"));
        clearBtn.setText(lang.getString("clear"));
        refreshBtn.setText(lang.getString("refresh"));
        backToLoginBtn.setText(lang.getString("backToLogin"));
        
        // ** PERBAIKAN DI SINI **
        // Gunakan referensi border yang sudah disimpan untuk mengubah judul
        formTitleBorder.setTitle(lang.getString("bookingData"));
        tableTitleBorder.setTitle(lang.getString("bookingList"));
        
        // Meminta panel untuk menggambar ulang agar judul yang baru muncul
        repaint();

        tableModel.setColumnIdentifiers(new String[]{
            "ID",
            lang.getString("tableNumber"),
            lang.getString("customerId"),
            lang.getString("bookingTime")
        });
        bookingTable.getColumnModel().getColumn(0).setMinWidth(0);
        bookingTable.getColumnModel().getColumn(0).setMaxWidth(0);
    }
}