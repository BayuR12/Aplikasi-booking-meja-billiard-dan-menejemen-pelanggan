package ui;

import controller.BookingController;
import dao.MongoBookingDAO;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

public final class BookingForm extends JPanel {
    private final BookingController bookingController;
    private final LanguageManager lang = LanguageManager.getInstance();

    // Deklarasikan komponen sebagai field
    private JLabel mejaLabel, pelangganLabel, waktuLabel;
    private JButton submitBtn;
    private final MainFrame mainFrame;

    public BookingForm(MainFrame frame) {
        this.mainFrame = frame;
        this.bookingController = new BookingController(new MongoBookingDAO());
        
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mejaLabel = new JLabel();
        JTextField mejaField = new JTextField();
        pelangganLabel = new JLabel();
        JTextField pelangganField = new JTextField();
        waktuLabel = new JLabel();
        JTextField waktuField = new JTextField();
        submitBtn = new JButton();
        JButton backToLoginBtn = new JButton();

        backToLoginBtn.addActionListener(e -> mainFrame.showPanel("login"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backToLoginBtn);
        buttonPanel.add(submitBtn);

        add(mejaLabel);
        add(mejaField);
        add(pelangganLabel);
        add(pelangganField);
        add(waktuLabel);
        add(waktuField);
        add(new JLabel()); 
        add(buttonPanel); 

        submitBtn.addActionListener(e -> {
            try {
                int meja = Integer.parseInt(mejaField.getText());
                int pelangganId = Integer.parseInt(pelangganField.getText());
                String waktuStr = waktuField.getText();

                if (waktuStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, lang.getString("allFieldsRequired"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = bookingController.buatBooking(meja, pelangganId, waktuStr);

                if (success) {
                    JOptionPane.showMessageDialog(this, lang.getString("bookingSuccess") + " " + meja, lang.getString("info"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, lang.getString("bookingFailed"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, lang.getString("invalidNumber"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, lang.getString("invalidTimeFormat"), lang.getString("error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        updateTexts(); // Atur teks awal
    }
    
    public void updateTexts(){
        mejaLabel.setText(lang.getString("tableNumber"));
        pelangganLabel.setText(lang.getString("customerId"));
        waktuLabel.setText(lang.getString("bookingTime"));
        submitBtn.setText(lang.getString("submitBooking"));
    }
}
