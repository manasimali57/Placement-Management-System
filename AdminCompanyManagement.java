package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AdminCompanyManagement {
    private static MongoClient mongoClient;

    public static void show(JFrame frame) {
        System.out.println("Showing AdminCompanyManagement");
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout(10, 10));

        // Header with Gradient and Logo
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
        JLabel headerLabel = new JLabel("Placement Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        navPanel.setOpaque(false);
        JButton logoutButton = new JButton("Logout");
        JButton committeeButton = new JButton("Committee Dashboard");
        JButton adminCompanyButton = new JButton("Admin Companies");
        styleButton(logoutButton, "logout-icon.png");
        styleButton(committeeButton, "dashboard-icon.png");
        styleButton(adminCompanyButton, "admin-icon.png");
        Dimension buttonSize = new Dimension(150, 45);
        logoutButton.setPreferredSize(buttonSize);
        committeeButton.setPreferredSize(buttonSize);
        adminCompanyButton.setPreferredSize(buttonSize);
        navPanel.add(logoutButton);
        navPanel.add(committeeButton);
        navPanel.add(adminCompanyButton);
        headerPanel.add(navPanel, BorderLayout.SOUTH);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Content with Background (wrapped in JScrollPane for scrollability)
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon("src/main/resources/bg.jpg").getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(255, 255, 255, 230)); // Lighter semi-transparent white overlay for better contrast
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Allow background to show through
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create("mongodb://localhost:27017");
            } catch (Exception ex) {
                System.out.println("MongoDB connection failed: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "MongoDB connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        MongoDatabase database = mongoClient.getDatabase("placement_db");
        MongoCollection<Document> companies = database.getCollection("companies");

        // Form Fields with High Contrast Labels
        JLabel nameLabel = new JLabel("Company Name:");
        nameLabel.setForeground(Color.BLACK); // Black text for contrast
        JTextField nameField = new JTextField(20);
        styleTextField(nameField);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        JLabel minCgpaLabel = new JLabel("Minimum CGPA:");
        minCgpaLabel.setForeground(Color.BLACK);
        JTextField minCgpaField = new JTextField(20);
        styleTextField(minCgpaField);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(minCgpaLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(minCgpaField, gbc);

        JLabel packageLabel = new JLabel("Package (LPA):");
        packageLabel.setForeground(Color.BLACK);
        JTextField packageField = new JTextField(20);
        styleTextField(packageField);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(packageLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(packageField, gbc);

        JLabel stipendLabel = new JLabel("Stipend:");
        stipendLabel.setForeground(Color.BLACK);
        JTextField stipendField = new JTextField(20);
        styleTextField(stipendField);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(stipendLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(stipendField, gbc);

        JLabel requirementsLabel = new JLabel("Requirements:");
        requirementsLabel.setForeground(Color.BLACK);
        JTextField requirementsField = new JTextField(20);
        styleTextField(requirementsField);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(requirementsLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(requirementsField, gbc);

        JLabel skillsLabel = new JLabel("Skills:");
        skillsLabel.setForeground(Color.BLACK);
        JTextField skillsField = new JTextField(20);
        styleTextField(skillsField);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(skillsLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(skillsField, gbc);

        JButton addButton = new JButton("Add Company");
        styleButton(addButton, "add-icon.png");
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(addButton, gbc);

        JButton backButton = new JButton("Back");
        styleButton(backButton, "back-icon.png");
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(backButton, gbc);

        // Company List
        JList<String> companyList = new JList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Document company : companies.find()) {
            listModel.addElement(company.getString("name") + " (CGPA: " + company.getString("minCgpa") +
                    ", Package: " + company.getString("package") + ", Stipend: " + company.getString("stipend") + ")");
        }
        companyList.setModel(listModel);
        companyList.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215)));
        companyList.setBackground(new Color(255, 255, 255));
        companyList.setFont(new Font("Arial", Font.PLAIN, 14));
        companyList.setForeground(Color.BLACK); // Ensure list text is visible
        JScrollPane scrollPane = new JScrollPane(companyList);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        formPanel.add(scrollPane, gbc);

        companyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = companyList.getSelectedValue();
                if (selected != null) {
                    String name = selected.split(" \\(CGPA: ")[0];
                    Document company = companies.find(Filters.eq("name", name)).first();
                    nameField.setText(company.getString("name"));
                    minCgpaField.setText(company.getString("minCgpa"));
                    packageField.setText(company.getString("package"));
                    stipendField.setText(company.getString("stipend"));
                    requirementsField.setText(company.getString("requirements"));
                    skillsField.setText(company.getString("skills"));
                }
            }
        });

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String minCgpa = minCgpaField.getText();
            String packageValue = packageField.getText();
            String stipend = stipendField.getText();
            String requirements = requirementsField.getText();
            String skills = skillsField.getText();
            if (!name.isEmpty() && !minCgpa.isEmpty() && !packageValue.isEmpty() && !stipend.isEmpty() && !requirements.isEmpty() && !skills.isEmpty()) {
                try {
                    Double.parseDouble(minCgpa); // Validate CGPA is a number
                    Document company = new Document("name", name)
                            .append("minCgpa", minCgpa)
                            .append("package", packageValue)
                            .append("stipend", stipend)
                            .append("requirements", requirements)
                            .append("skills", skills);
                    companies.insertOne(company);
                    listModel.addElement(name + " (CGPA: " + minCgpa + ", Package: " + packageValue + ", Stipend: " + stipend + ")");
                    JOptionPane.showMessageDialog(frame, "Company added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    nameField.setText("");
                    minCgpaField.setText("");
                    packageField.setText("");
                    stipendField.setText("");
                    requirementsField.setText("");
                    skillsField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Minimum CGPA must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            System.out.println("Navigating back to LoginPage");
            LoginPage.show(frame);
        });

        logoutButton.addActionListener(e -> {
            System.out.println("Navigating to LoginPage");
            try {
                LoginPage.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing LoginPage: " + ex.getMessage());
            }
        });

        committeeButton.addActionListener(e -> {
            System.out.println("Navigating to CommitteeDashboard");
            try {
                CommitteeDashboard.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing CommitteeDashboard: " + ex.getMessage());
            }
        });

        adminCompanyButton.addActionListener(e -> AdminCompanyManagement.show(frame));

        mainPanel.add(formPanel, BorderLayout.CENTER);
        JScrollPane scrollPaneMain = new JScrollPane(mainPanel); // Wrap mainPanel in JScrollPane
        scrollPaneMain.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPaneMain, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
        System.out.println("AdminCompanyManagement frame updated");
    }

    private static void styleButton(JButton button, String iconPath) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 120, 215), 1, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 90, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });
        try {
            button.setIcon(new ImageIcon("src/main/resources/" + iconPath));
            button.setHorizontalTextPosition(JButton.RIGHT);
        } catch (Exception ex) {
            System.out.println("Icon '" + iconPath + "' not found: " + ex.getMessage());
        }
    }

    private static void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setBackground(Color.WHITE);
    }
}
