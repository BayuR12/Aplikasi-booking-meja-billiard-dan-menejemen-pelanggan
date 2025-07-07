package ui;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import util.MongoUtil;

public final class LogPanel extends JPanel {
    private final MainFrame mainFrame;
    private final LanguageManager lang = LanguageManager.getInstance();
    private final JTable logTable;
    private final DefaultTableModel tableModel;
    private final JButton backButton;
    private final JLabel titleLabel;

    public LogPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        logTable = new JTable(tableModel);
        add(new JScrollPane(logTable), BorderLayout.CENTER);
        
        backButton = new JButton();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener(e -> mainFrame.showPanel("booking"));

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                loadLogs();
            }
        });
        
        updateTexts();
    }

    private void loadLogs() {
        tableModel.setRowCount(0);
        try {
            MongoCollection<Document> logs = MongoUtil.getDatabase().getCollection("logs");
            try (MongoCursor<Document> cursor = logs.find().sort(new Document("timestamp", -1)).iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    String activity = doc.getString("activity");
                    java.util.Date timestamp = doc.getDate("timestamp");
                    
                    String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(timestamp);
                    
                    tableModel.addRow(new Object[]{activity, formattedDate});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data log: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateTexts() {
        titleLabel.setText(lang.getString("viewLogsMenu"));
        backButton.setText(lang.getString("backToBooking"));
        tableModel.setColumnIdentifiers(new String[]{lang.getString("logActivity"), lang.getString("logTimestamp")});
    }
}