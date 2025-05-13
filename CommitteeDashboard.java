package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CommitteeDashboard {
    private static MongoClient mongoClient;

    public static void show(JFrame frame) {
        System.out.println("Showing CommitteeDashboard");
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        // Initialize MongoDB client
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create("mongodb://localhost:27017");
                System.out.println("MongoDB client initialized successfully");
            } catch (Exception ex) {
                System.out.println("MongoDB connection failed: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "MongoDB connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        MongoDatabase database = mongoClient.getDatabase("placement_db");
        System.out.println("Using database: " + database.getName());

        // Header with Gradient and Logo
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 120, 215), 0, getHeight(), new Color(0, 50, 150));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                Image logo = new ImageIcon("src/main/resources/logo.png").getImage();
                if (logo != null) {
                    g2d.drawImage(logo, 20, 10, 80, 80, this);
                }
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 120));
        JLabel headerLabel = new JLabel("Placement Management System", JLabel.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        navPanel.setBackground(new Color(0, 120, 215));
        JButton logoutButton = new JButton("Logout");
        JButton committeeButton = new JButton("Committee Dashboard");
        JButton adminCompanyButton = new JButton("Admin Companies");
        styleButton(logoutButton, "logout-icon.png");
        styleButton(committeeButton, "dashboard-icon.png");
        styleButton(adminCompanyButton, "admin-icon.png");
        logoutButton.setPreferredSize(new Dimension(150, 50));
        committeeButton.setPreferredSize(new Dimension(200, 50));
        adminCompanyButton.setPreferredSize(new Dimension(200, 50));
        navPanel.add(logoutButton);
        navPanel.add(committeeButton);
        navPanel.add(adminCompanyButton);
        frame.add(navPanel, BorderLayout.NORTH);

        // Main Content
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon("src/main/resources/bg.jpg").getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(255, 255, 255, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        JLabel label = new JLabel("Committee Dashboard for " + LoginPage.getCurrentUsername(), JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(new Color(0, 120, 215));
        mainPanel.add(label, BorderLayout.NORTH);

        // Student Applications Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Student Applications", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 22));
        tableTitle.setForeground(new Color(0, 120, 215));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Fetch student data
        List<Object[]> tableData = new ArrayList<>();
        try {
            MongoCollection<Document> studentsCollection = database.getCollection("students");
            System.out.println("Querying students collection...");
            FindIterable<Document> students = studentsCollection.find();
            int studentCount = 0;
            for (Document student : students) {
                studentCount++;
                System.out.println("Found student #" + studentCount + ": " + student.toJson());
                String studentName = student.getString("name");
                String username = student.getString("username");
                String cgpa = student.getString("cgpa");
                String status = student.getString("status");

                // Fetch application details if available
                MongoCollection<Document> applicationsCollection = database.getCollection("placement_applications");
                Document application = applicationsCollection.find(Filters.eq("username", username)).first();
                String companyName = (application != null && application.getString("company") != null)
                        ? application.getString("company")
                        : "N/A";

                tableData.add(new Object[]{studentName, companyName, cgpa, status, username});
            }
            System.out.println("Total students found: " + studentCount);
            System.out.println("Table data size: " + tableData.size());
        } catch (Exception e) {
            System.out.println("Error fetching student data from MongoDB: " + e.getMessage());
            JOptionPane.showMessageDialog(frame, "Error fetching student data", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Table setup with editable columns
        String[] columns = {"Student Name", "Company Name", "CGPA", "Status", "Actions"};
        Object[][] data = (tableData.isEmpty()) ? new Object[0][columns.length] : tableData.toArray(new Object[0][]);
        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 2 || column == 3 || column == 4; // Editable: Student Name, CGPA, Status, Actions
            }
        };
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 248, 255));
                } else {
                    c.setBackground(Color.WHITE);
                }
                if (isCellSelected(row, column)) {
                    c.setBackground(new Color(173, 216, 230));
                }
                return c;
            }
        };
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(35);
        table.setGridColor(new Color(0, 120, 215));
        table.setShowGrid(true);
        table.setBackground(new Color(255, 255, 255, 230));
        table.setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(0, 120, 215));
        table.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Custom renderer and editor for the Actions column
        table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            private JPanel panel = new JPanel();
            private JButton editButton = new JButton("Edit");
            private JButton statusButton = new JButton("Change Status");

            {
                panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                styleButton(editButton, null);
                styleButton(statusButton, null);
                editButton.setPreferredSize(new Dimension(80, 25));
                statusButton.setPreferredSize(new Dimension(100, 25));
                panel.add(editButton);
                panel.add(statusButton);

                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int row = table.getSelectedRow();
                        if (row != -1) {
                            table.editCellAt(row, 0); // Start editing Student Name
                        }
                    }
                });

                statusButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int row = table.getSelectedRow();
                        String username = (String) tableModel.getValueAt(row, 4);
                        changeStatus(frame, database, username);
                    }
                });
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return panel;
            }
        });

        // Save changes when editing is done
        table.putClientProperty("terminateEditOnFocusLost", true);
        table.addPropertyChangeListener("tableCellEditor", evt -> {
            if (table.getCellEditor() != null && !table.getCellEditor().stopCellEditing()) {
                return;
            }
            int row = table.getEditingRow();
            int column = table.getEditingColumn();
            if (row != -1 && column != -1 && column != 4) { // Skip Actions column
                String username = (String) tableModel.getValueAt(row, 4);
                String newValue = tableModel.getValueAt(row, column).toString();
                MongoCollection<Document> studentsCollection = database.getCollection("students");
                MongoCollection<Document> applicationsCollection = database.getCollection("placement_applications");
                switch (column) {
                    case 0: // Student Name
                        studentsCollection.updateOne(Filters.eq("username", username), Updates.set("name", newValue));
                        break;
                    case 2: // CGPA
                        studentsCollection.updateOne(Filters.eq("username", username), Updates.set("cgpa", newValue));
                        break;
                    case 3: // Status
                        studentsCollection.updateOne(Filters.eq("username", username), Updates.set("status", newValue));
                        Document application = applicationsCollection.find(Filters.eq("username", username)).first();
                        if (application != null) {
                            applicationsCollection.updateOne(Filters.eq("username", username), Updates.set("status", newValue));
                        }
                        break;
                }
                System.out.println("Updated " + columns[column] + " to " + newValue + " for username " + username);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button actions
        logoutButton.addActionListener(e -> {
            System.out.println("Navigating to LoginPage");
            try {
                LoginPage.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing LoginPage: " + ex.getMessage());
            }
        });

        committeeButton.addActionListener(e -> CommitteeDashboard.show(frame));

        adminCompanyButton.addActionListener(e -> {
            System.out.println("Navigating to AdminCompanyManagement");
            try {
                AdminCompanyManagement.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing AdminCompanyManagement: " + ex.getMessage());
            }
        });

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        System.out.println("CommitteeDashboard frame updated");
    }

    private static void changeStatus(JFrame frame, MongoDatabase database, String username) {
        MongoCollection<Document> studentsCollection = database.getCollection("students");
        Document student = studentsCollection.find(Filters.eq("username", username)).first();
        if (student != null) {
            String[] options = {"Pending", "Approved", "Rejected"};
            String currentStatus = student.getString("status");
            int choice = JOptionPane.showOptionDialog(frame, "Select new status:", "Change Status",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, currentStatus);
            if (choice >= 0) {
                String newStatus = options[choice];
                studentsCollection.updateOne(Filters.eq("username", username), Updates.set("status", newStatus));
                MongoCollection<Document> applicationsCollection = database.getCollection("placement_applications");
                Document application = applicationsCollection.find(Filters.eq("username", username)).first();
                if (application != null) {
                    applicationsCollection.updateOne(Filters.eq("username", username), Updates.set("status", newStatus));
                }
                show(frame); // Refresh the dashboard
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void styleButton(JButton button, String iconPath) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 90, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });
        if (iconPath != null) {
            try {
                button.setIcon(new ImageIcon("src/main/resources/" + iconPath));
                button.setHorizontalTextPosition(JButton.RIGHT);
            } catch (Exception ex) {
                System.out.println("Icon '" + iconPath + "' not found: " + ex.getMessage());
            }
        }
    }

    public static void closeMongoClient() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB client closed");
        }
    }
}
