package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PlacementApplication {
    private static MongoClient mongoClient;

    public static void show(JFrame frame) {
        System.out.println("Showing PlacementApplication");
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // Full-screen Background
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon("src/main/resources/bg.jpg").getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent overlay
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false); // Transparent to show background
        mainPanel.setBorder(new EmptyBorder(60, 60, 60, 60));

        // Header
        JLabel headerLabel = new JLabel("Placement Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setOpaque(false);
        JButton dashboardButton = new JButton("Dashboard");
        JButton profileButton = new JButton("Profile");
        JButton applyButton = new JButton("Apply");
        JButton logoutButton = new JButton("Logout");
        styleButton(dashboardButton, "dashboard-icon.png");
        styleButton(profileButton, "profile-icon.png");
        styleButton(applyButton, "apply-icon.png");
        styleButton(logoutButton, "logout-icon.png");
        Dimension buttonSize = new Dimension(120, 40); // Wider buttons for full text
        dashboardButton.setPreferredSize(buttonSize);
        profileButton.setPreferredSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);
        navPanel.add(dashboardButton);
        navPanel.add(profileButton);
        navPanel.add(applyButton);
        navPanel.add(logoutButton);
        mainPanel.add(navPanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
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
        MongoCollection<Document> students = database.getCollection("students");
        MongoCollection<Document> applications = database.getCollection("placement_applications");

        Document student = students.find(Filters.eq("username", LoginPage.getCurrentUsername())).first();
        String studentCgpa = student != null ? student.getString("cgpa") : "0.0";
        double cgpa = studentCgpa.isEmpty() ? 0.0 : Double.parseDouble(studentCgpa);

        JLabel header = new JLabel("Apply for Placement", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(header, gbc);

        JComboBox<String> companyDropdown = new JComboBox<>();
        companyDropdown.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        for (Document company : companies.find()) {
            String name = company.getString("name");
            String minCgpa = company.getString("minCgpa");
            double minCgpaValue = Double.parseDouble(minCgpa);
            if (cgpa >= minCgpaValue) {
                companyDropdown.addItem(name);
            }
        }
        if (companyDropdown.getItemCount() == 0) {
            companyDropdown.addItem("No eligible companies");
            companyDropdown.setEnabled(false);
        }
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(companyDropdown, gbc);

        JLabel eligibilityLabel = new JLabel("Eligibility: Your CGPA: " + String.format("%.1f", cgpa));
        eligibilityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        eligibilityLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(eligibilityLabel, gbc);

        JTextArea companyInfo = new JTextArea(5, 40);
        companyInfo.setEditable(false);
        companyInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        companyInfo.setForeground(Color.WHITE);
        companyInfo.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent background
        companyInfo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        companyDropdown.addActionListener(e -> {
            String selectedCompany = (String) companyDropdown.getSelectedItem();
            if (selectedCompany != null && !selectedCompany.equals("No eligible companies")) {
                Document company = companies.find(Filters.eq("name", selectedCompany)).first();
                if (company != null) {
                    String packageInfo = company.getString("package");
                    String stipend = company.getString("stipend");
                    String requirements = company.getString("requirements");
                    String skills = company.getString("skills");
                    companyInfo.setText(String.format(
                            "Company: %s\nPackage: %s LPA\nStipend: %s\nRequirements: %s\nSkills: %s",
                            selectedCompany, packageInfo, stipend, requirements, skills));
                }
            } else {
                companyInfo.setText("No company selected or eligible.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(companyInfo, gbc);

        applyButton = new JButton("Apply");
        styleButton(applyButton, "apply-icon.png");
        applyButton.setPreferredSize(new Dimension(100, 40)); // Ensure full text
        JButton backButton = new JButton("Back");
        styleButton(backButton, "back-icon.png");
        backButton.setPreferredSize(new Dimension(100, 40)); // Ensure full text
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(applyButton);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        applyButton.addActionListener(e -> {
            String selectedCompany = (String) companyDropdown.getSelectedItem();
            if (selectedCompany != null && !selectedCompany.equals("No eligible companies")) {
                Document company = companies.find(Filters.eq("name", selectedCompany)).first();
                String minCgpa = company.getString("minCgpa");
                double minCgpaValue = Double.parseDouble(minCgpa);
                if (cgpa >= minCgpaValue) {
                    Document application = new Document("username", LoginPage.getCurrentUsername())
                            .append("company", selectedCompany)
                            .append("status", "Pending")
                            .append("appliedDate", new java.util.Date().toString());
                    applications.insertOne(application);
                    System.out.println("Application submitted for " + selectedCompany);
                    JOptionPane.showMessageDialog(frame, "Application submitted for " + selectedCompany, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "You are not eligible. Your CGPA (" + String.format("%.1f", cgpa) + ") is below the required " + minCgpa + ".", "Eligibility Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> {
            System.out.println("Navigating back to StudentDashboard");
            StudentDashboard.show(frame);
        });

        dashboardButton.addActionListener(e -> {
            System.out.println("Navigating to StudentDashboard");
            StudentDashboard.show(frame);
        });
        profileButton.addActionListener(e -> {
            System.out.println("Navigating to StudentProfile");
            try {
                StudentProfile.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing StudentProfile: " + ex.getMessage());
            }
        });
        applyButton.addActionListener(e -> PlacementApplication.show(frame));
        logoutButton.addActionListener(e -> {
            System.out.println("Navigating to LoginPage");
            try {
                LoginPage.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing LoginPage: " + ex.getMessage());
            }
        });

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
        System.out.println("PlacementApplication frame updated");
    }

    private static void styleButton(JButton button, String iconPath) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.LIGHT_GRAY);
            }
        });
        try {
            button.setIcon(new ImageIcon("src/main/resources/" + iconPath));
            button.setHorizontalTextPosition(JButton.RIGHT);
        } catch (Exception ex) {
            System.out.println("Icon '" + iconPath + "' not found: " + ex.getMessage());
        }
    }
}
