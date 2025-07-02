package ui;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import util.MongoUtil;

public final class LogPanel extends JPanel {
    private final MainFrame mainFrame;
    private final LanguageManager lang = LanguageManager.getInstance();
    private final JTable logTable;
    private final DefaultTableModel tableModel;
    private final JScrollPane scrollPane;
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

        // Setup tabel
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Activity");
        tableModel.addColumn("Timestamp");
        logTable = new JTable(tableModel);
        scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Tombol kembali
        backButton = new JButton();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener(e -> mainFrame.showPanel("booking"));

        // Muat data saat panel ditampilkan
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                loadLogs();
            }
        });
        
        updateTexts();
    }

    private void loadLogs() {
        // Hapus data lama
        tableModel.setRowCount(0);
        // Ambil data baru dari MongoDB
        MongoCollection<Document> logs = MongoUtil.getDatabase().getCollection("logs");
        try (MongoCursor<Document> cursor = logs.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                tableModel.addRow(new Object[]{doc.getString("activity"), doc.getDate("timestamp").toString()});
            }
        }
    }

    public void updateTexts() {
        titleLabel.setText(lang.getString("viewLogsMenu"));
        backButton.setText(lang.getString("backToBooking")); // Anda perlu menambahkan kunci ini
        tableModel.setColumnIdentifiers(new String[]{lang.getString("logActivity"), lang.getString("logTimestamp")}); // Dan ini
    }
}
