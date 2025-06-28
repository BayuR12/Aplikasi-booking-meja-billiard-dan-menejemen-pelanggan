package ui;

import controller.BookingController;
import dao.MongoBookingDAO;
import service.MongoLogService; // <-- Tambahkan impor ini
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

public final class BookingForm extends JPanel {
    private final MainFrame mainFrame;
    private final BookingController bookingController;
    private final LanguageManager lang = LanguageManager.getInstance();

    private final JLabel mejaLabel = new JLabel();
    private final JLabel pelangganLabel = new JLabel();
    private final JLabel waktuLabel = new JLabel();
    private final JTextField mejaField = new JTextField(10);
    private final JTextField pelangganField = new JTextField(10);
    private final JTextField waktuField = new JTextField(10);
    private final JButton submitBtn = new JButton();

    public BookingForm(MainFrame frame) {
        this.mainFrame = frame;
        this.bookingController = new BookingController(new MongoBookingDAO());
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; add(mejaLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(mejaField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(pelangganLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(pelangganField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(waktuLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(waktuField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; add(submitBtn, gbc);
        waktuField.setToolTipText(lang.getString("timeFormatTooltip"));

        submitBtn.addActionListener(e -> {
            try {
                int meja = Integer.parseInt(mejaField.getText());
                int pelangganId = Integer.parseInt(pelangganField.getText());
                String waktuStr = waktuField.getText();

                if (waktuStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, lang.getString("emptyFields"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = bookingController.buatBooking(meja, pelangganId, waktuStr);

                if (success) {
                    MongoLogService.log("Booking created for table " + meja + " by customer ID " + pelangganId); // <-- Tambahkan log
                    JOptionPane.showMessageDialog(this, lang.getString("bookingSuccess") + " " + meja, lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    MongoLogService.log("Failed booking attempt for table " + meja); // <-- Tambahkan log
                    JOptionPane.showMessageDialog(this, lang.getString("bookingFailed"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, lang.getString("invalidNumber"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, lang.getString("invalidTimeFormat"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        updateTexts();
    }
    
    public void updateTexts() {
        mejaLabel.setText(lang.getString("tableNumber"));
        pelangganLabel.setText(lang.getString("customerID"));
        waktuLabel.setText(lang.getString("time"));
        submitBtn.setText(lang.getString("submit"));
        waktuField.setToolTipText(lang.getString("timeFormatTooltip"));
        mainFrame.updateLanguageMenuItem();
    }
}