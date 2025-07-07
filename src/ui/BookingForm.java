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

    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton addOrUpdateBtn, clearBtn, backToLoginBtn, refreshBtn, deleteBtn;
    private JTextField mejaField, pelangganField, waktuField;
    private JLabel mejaLabel, pelangganLabel, waktuLabel;
    private String selectedBookingId = null;

    private TitledBorder formTitleBorder;
    private TitledBorder tableTitleBorder;

    public BookingForm(MainFrame frame) {
        this.mainFrame = frame;
        this.bookingController = new BookingController(new MongoBookingDAO());
        
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formTitleBorder = BorderFactory.createTitledBorder("");
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

        JPanel tablePanel = new JPanel(new BorderLayout());
        tableTitleBorder = BorderFactory.createTitledBorder("");
        tablePanel.setBorder(tableTitleBorder);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton();
        deleteBtn = new JButton();
        backToLoginBtn = new JButton();
        bottomButtonPanel.add(refreshBtn);
        bottomButtonPanel.add(deleteBtn);
        bottomButtonPanel.add(backToLoginBtn);

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomButtonPanel, BorderLayout.SOUTH);

        backToLoginBtn.addActionListener(e -> mainFrame.showPanel("login"));
        refreshBtn.addActionListener(e -> {
            loadBookings();
            clearForm();
        });
        clearBtn.addActionListener(e -> clearForm());
        addOrUpdateBtn.addActionListener(e -> saveBooking());
        deleteBtn.addActionListener(e -> deleteBooking());

        bookingTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && bookingTable.getSelectedRow() != -1) {
                populateFormFromTable();
            }
        });
        
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

            if (selectedBookingId == null) {
                // Tambah Booking Baru
                boolean success = bookingController.buatBooking(meja, pelangganId, waktuStr);
                if (success) {
                    JOptionPane.showMessageDialog(this, lang.getString("bookingAddedSuccess"), lang.getString("success"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, lang.getString("bookingAddedFailed"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Perbarui Booking
                boolean success = bookingController.updateBooking(selectedBookingId, meja, pelangganId, waktuStr);
                if (success) {
                    JOptionPane.showMessageDialog(this, lang.getString("bookingUpdatedSuccess"), lang.getString("success"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, lang.getString("bookingUpdatedFailed"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
            loadBookings();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Table number and Customer ID must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use YYYY-MM-DDTHH:MM:SS", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBooking() {
        if (selectedBookingId == null) {
            JOptionPane.showMessageDialog(this, lang.getString("selectBookingToDelete"), lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            lang.getString("confirmDeletionMessage"),
            lang.getString("confirmDeletionTitle"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            bookingController.deleteBooking(selectedBookingId);
            JOptionPane.showMessageDialog(this, lang.getString("bookingDeletedSuccess"), lang.getString("success"), JOptionPane.INFORMATION_MESSAGE);
            loadBookings();
            clearForm();
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
        formTitleBorder.setTitle(lang.getString("bookingData"));
        tableTitleBorder.setTitle(lang.getString("bookingList"));
        
        mejaLabel.setText(lang.getString("tableNumber"));
        pelangganLabel.setText(lang.getString("customerId"));
        waktuLabel.setText(lang.getString("bookingTime"));
        
        addOrUpdateBtn.setText(lang.getString("addBooking"));
        clearBtn.setText(lang.getString("clear"));
        refreshBtn.setText(lang.getString("refresh"));
        deleteBtn.setText(lang.getString("deleteBooking"));
        backToLoginBtn.setText(lang.getString("backToLogin"));
        
        tableModel.setColumnIdentifiers(new String[]{
            "ID",
            lang.getString("tableNumber"),
            lang.getString("customerId"),
            lang.getString("bookingTime")
        });
        
        bookingTable.getColumnModel().getColumn(0).setMinWidth(0);
        bookingTable.getColumnModel().getColumn(0).setMaxWidth(0);
        bookingTable.getColumnModel().getColumn(0).setWidth(0);

        repaint();
    }
}