package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class StudentApplicationTracking {
    public static void show(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        // Enhanced Header
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 120, 215), getWidth(), 0, new Color(30, 144, 255));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
                Image logo = new ImageIcon("src/main/resources/logo.png").getImage();
                if (logo != null) {
                    g2d.drawImage(logo, 20, 10, 80, 80, this);
                }
            }
        };
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 120));
        String username = LoginPage.getCurrentUsername() != null ? LoginPage.getCurrentUsername() : "Student";
        JLabel headerLabel = new JLabel("Application Tracking - " + username, JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        navPanel.setOpaque(false);
        JButton dashboardBtn = new JButton("Dashboard");
        JButton profileBtn = new JButton("Profile");
        JButton logoutBtn = new JButton("Logout");
        Dimension buttonSize = new Dimension(150, 45);
        dashboardBtn.setPreferredSize(buttonSize);
        profileBtn.setPreferredSize(buttonSize);
        logoutBtn.setPreferredSize(buttonSize);
        styleHeaderButton(dashboardBtn);
        styleHeaderButton(profileBtn);
        styleHeaderButton(logoutBtn);
        navPanel.add(dashboardBtn);
        navPanel.add(profileBtn);
        navPanel.add(logoutBtn);
        headerPanel.add(navPanel, BorderLayout.SOUTH);
        frame.add(headerPanel, BorderLayout.NORTH);

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
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Application Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Your Applications", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 24));
        tableTitle.setForeground(new Color(0, 120, 215));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Updated table model without "CTC" and "Role" columns
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Company", "Status"}, 0);
        JTable applicationTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationTable.setFont(new Font("Arial", Font.PLAIN, 14));
        applicationTable.setRowHeight(30);
        applicationTable.setGridColor(new Color(0, 120, 215));
        applicationTable.setShowGrid(true);
        applicationTable.setBackground(new Color(255, 255, 255, 230));
        applicationTable.setForeground(Color.BLACK);
        applicationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        applicationTable.getTableHeader().setBackground(new Color(0, 120, 215));
        applicationTable.getTableHeader().setForeground(Color.WHITE);
        applicationTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        // Fetch and populate application data
        String studentUsername = LoginPage.getCurrentUsername();
        if (studentUsername != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("placement_db");
                MongoCollection<Document> applications = database.getCollection("placement_applications");
                List<Document> appDocs = applications.find(Filters.eq("username", studentUsername)).into(new ArrayList<>());
                for (Document app : appDocs) {
                    String company = app.getString("company");
                    String status = app.getString("status");
                    tableModel.addRow(new Object[]{company, status});
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error fetching applications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No user logged in", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(applicationTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Actions
        dashboardBtn.addActionListener(e -> new StudentDashboard().show(frame));
        profileBtn.addActionListener(e -> {
            System.out.println("Navigating to StudentProfile");
            try {
                StudentProfile.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing StudentProfile: " + ex.getMessage());
            }
        });
        logoutBtn.addActionListener(e -> new LoginPage().show(frame));

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    private static void styleHeaderButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 120, 215), 1, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 90, 160));
                button.setPreferredSize(new Dimension(155, 50));
                button.revalidate();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 120, 215));
                button.setPreferredSize(new Dimension(150, 45));
                button.revalidate();
            }
        });
    }

    // Custom Renderer Class to Handle Cloning
    static class CustomTableCellRenderer extends DefaultTableCellRenderer implements Cloneable {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                c.setBackground(new Color(240, 248, 255));
            } else {
                c.setBackground(Color.WHITE);
            }
            if (isSelected) {
                c.setBackground(new Color(173, 216, 230));
            }
            ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
            return c;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
