package ui;

import controller.BookingController;
import dao.MongoBookingDAO;

import javax.swing.*;
import java.awt.*;

public class BookingForm extends JPanel {
    private final BookingController bookingController;

    public BookingForm() {
        this.bookingController = new BookingController(new MongoBookingDAO());
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel mejaLabel = new JLabel("No. Meja:");
        JTextField mejaField = new JTextField();
        JLabel pelangganLabel = new JLabel("ID Pelanggan:");
        JTextField pelangganField = new JTextField(); // Variabel dideklarasikan di sini
        JLabel waktuLabel = new JLabel("Waktu Booking (YYYY-MM-DDTHH:MM):");
        JTextField waktuField = new JTextField();
        JButton submitBtn = new JButton("Booking");

        submitBtn.addActionListener(e -> {
            try {
                int meja = Integer.parseInt(mejaField.getText());
                int pelangganId = Integer.parseInt(pelangganField.getText());
                String waktuStr = waktuField.getText();

                if (waktuStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Waktu booking harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = bookingController.buatBooking(meja, pelangganId, waktuStr);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Booking berhasil dibuat untuk Meja: " + meja);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal membuat booking. Meja mungkin sudah dipesan pada waktu tersebut.", "Error Booking", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nomor Meja dan ID Pelanggan harus berupa angka.", "Error Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(mejaLabel);
        add(mejaField);
        add(pelangganLabel);
        add(pelangganField); // <<<< PERBAIKAN DI SINI: sebelumnya 'pelangField'
        add(waktuLabel);
        add(waktuField);
        add(new JLabel());
        add(submitBtn);
    }
}