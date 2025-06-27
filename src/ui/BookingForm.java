package ui;

import javax.swing.*;
import java.awt.*;

public class BookingForm extends JPanel {
    public BookingForm() {
        setLayout(new GridLayout(4, 2));

        JLabel mejaLabel = new JLabel("No. Meja:");
        JTextField mejaField = new JTextField();
        JLabel pelangganLabel = new JLabel("ID Pelanggan:");
        JTextField pelangganField = new JTextField();
        JLabel waktuLabel = new JLabel("Waktu Booking (YYYY-MM-DDThh:mm):");
        JTextField waktuField = new JTextField();
        JButton submitBtn = new JButton("Booking");

        submitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Booking dibuat untuk Meja: " + mejaField.getText());
        });

        add(mejaLabel); add(mejaField);
        add(pelangganLabel); add(pelangganField);
        add(waktuLabel); add(waktuField);
        add(new JLabel()); add(submitBtn);
    }
}