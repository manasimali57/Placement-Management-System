package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class StudentProfile {
    private static MongoClient mongoClient;

    public static void show(JFrame frame) {
        System.out.println("Showing StudentProfile");
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
        MongoCollection<Document> students = database.getCollection("students");
        final Document[] student = {students.find(Filters.eq("username", LoginPage.getCurrentUsername())).first()};
        System.out.println("Student data found: " + student[0]);

        // Form Fields
        JLabel nameLabel = new JLabel("Enter Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(student[0] != null ? student[0].getString("name") : "", 20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        JLabel prnLabel = new JLabel("Enter PRN:");
        prnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        prnLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(prnLabel, gbc);

        JTextField prnField = new JTextField(student[0] != null ? student[0].getString("prn") : "", 20);
        prnField.setFont(new Font("Arial", Font.PLAIN, 14));
        prnField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(prnField, gbc);

        JLabel semesterLabel = new JLabel("Enter Semester:");
        semesterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        semesterLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(semesterLabel, gbc);

        JTextField semesterField = new JTextField(student[0] != null ? student[0].getString("semester") : "", 20);
        semesterField.setFont(new Font("Arial", Font.PLAIN, 14));
        semesterField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(semesterField, gbc);

        JLabel branchLabel = new JLabel("Enter Branch:");
        branchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        branchLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(branchLabel, gbc);

        JTextField branchField = new JTextField(student[0] != null ? student[0].getString("branch") : "", 20);
        branchField.setFont(new Font("Arial", Font.PLAIN, 14));
        branchField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(branchField, gbc);

        JLabel cgpaLabel = new JLabel("Enter CGPA:");
        cgpaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cgpaLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(cgpaLabel, gbc);

        JTextField cgpaField = new JTextField(student[0] != null ? student[0].getString("cgpa") : "", 20);
        cgpaField.setFont(new Font("Arial", Font.PLAIN, 14));
        cgpaField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(cgpaField, gbc);

        JLabel backlogsLabel = new JLabel("Enter Number of Backlogs:");
        backlogsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        backlogsLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(backlogsLabel, gbc);

        JTextField backlogsField = new JTextField(student[0] != null ? student[0].getString("backlogs") : "", 20);
        backlogsField.setFont(new Font("Arial", Font.PLAIN, 14));
        backlogsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(backlogsField, gbc);

        JLabel commentsLabel = new JLabel("Enter Comments:");
        commentsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        commentsLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(commentsLabel, gbc);

        JTextField commentsField = new JTextField(student[0] != null ? student[0].getString("comments") : "", 20);
        commentsField.setFont(new Font("Arial", Font.PLAIN, 14));
        commentsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formPanel.add(commentsField, gbc);

        JLabel resumeLabel = new JLabel("Enter Resume PDF:");
        resumeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resumeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(resumeLabel, gbc);

        JTextField resumeField = new JTextField(student[0] != null ? student[0].getString("resume") : "", 20);
        resumeField.setFont(new Font("Arial", Font.PLAIN, 14));
        resumeField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        resumeField.setEditable(false);
        JButton uploadButton = new JButton("Upload");
        styleButton(uploadButton, "upload-icon.png");
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                resumeField.setText(selectedFile.getAbsolutePath());
            }
        });
        JPanel resumePanel = new JPanel(new BorderLayout());
        resumePanel.add(resumeField, BorderLayout.CENTER);
        resumePanel.add(uploadButton, BorderLayout.EAST);
        gbc.gridx = 1;
        formPanel.add(resumePanel, gbc);

        // Buttons
        JButton saveButton = new JButton("Save");
        styleButton(saveButton, "save-icon.png");
        saveButton.setPreferredSize(new Dimension(100, 40));
        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, "refresh-icon.png");
        refreshButton.setPreferredSize(new Dimension(100, 40));
        JButton backButton = new JButton("Back");
        styleButton(backButton, "back-icon.png");
        backButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(saveButton);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Actions
        saveButton.addActionListener(e -> {
            String username = LoginPage.getCurrentUsername();
            String name = nameField.getText().trim();
            String prn = prnField.getText().trim();
            String semester = semesterField.getText().trim();
            String branch = branchField.getText().trim();
            String cgpa = cgpaField.getText().trim();
            String backlogs = backlogsField.getText().trim();
            String comments = commentsField.getText().trim();
            String resume = resumeField.getText().trim();

            if (name.isEmpty() || prn.isEmpty() || semester.isEmpty() || branch.isEmpty() || cgpa.isEmpty() || backlogs.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields except comments and resume are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Double.parseDouble(cgpa); // Validate CGPA
                Integer.parseInt(backlogs); // Validate backlogs
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "CGPA must be a valid number and Backlogs must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Document updatedStudent = new Document("username", username)
                    .append("name", name)
                    .append("prn", prn)
                    .append("semester", semester)
                    .append("branch", branch)
                    .append("cgpa", cgpa)
                    .append("backlogs", backlogs)
                    .append("comments", comments)
                    .append("resume", resume.isEmpty() ? (student[0] != null ? student[0].getString("resume") : "") : resume);

            if (student[0] == null) {
                students.insertOne(updatedStudent);
                System.out.println("New student profile inserted");
            } else {
                students.updateOne(Filters.eq("username", username),
                        Updates.combine(
                                Updates.set("name", name),
                                Updates.set("prn", prn),
                                Updates.set("semester", semester),
                                Updates.set("branch", branch),
                                Updates.set("cgpa", cgpa),
                                Updates.set("backlogs", backlogs),
                                Updates.set("comments", comments),
                                Updates.set("resume", resume.isEmpty() ? student[0].getString("resume") : resume)
                        ));
                System.out.println("Student profile updated");
            }
            JOptionPane.showMessageDialog(frame, "Profile saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Refresh fields with updated data
            student[0] = students.find(Filters.eq("username", username)).first();
            nameField.setText(student[0].getString("name"));
            prnField.setText(student[0].getString("prn"));
            semesterField.setText(student[0].getString("semester"));
            branchField.setText(student[0].getString("branch"));
            cgpaField.setText(student[0].getString("cgpa"));
            backlogsField.setText(student[0].getString("backlogs"));
            commentsField.setText(student[0].getString("comments"));
            resumeField.setText(student[0].getString("resume"));
        });

        refreshButton.addActionListener(e -> {
            System.out.println("Refreshing StudentProfile");
            student[0] = students.find(Filters.eq("username", LoginPage.getCurrentUsername())).first();
            if (student[0] != null) {
                nameField.setText(student[0].getString("name"));
                prnField.setText(student[0].getString("prn"));
                semesterField.setText(student[0].getString("semester"));
                branchField.setText(student[0].getString("branch"));
                cgpaField.setText(student[0].getString("cgpa"));
                backlogsField.setText(student[0].getString("backlogs"));
                commentsField.setText(student[0].getString("comments"));
                resumeField.setText(student[0].getString("resume"));
            } else {
                nameField.setText("");
                prnField.setText("");
                semesterField.setText("");
                branchField.setText("");
                cgpaField.setText("");
                backlogsField.setText("");
                commentsField.setText("");
                resumeField.setText("");
                JOptionPane.showMessageDialog(frame, "No profile data found", "Info", JOptionPane.INFORMATION_MESSAGE);
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
        profileButton.addActionListener(e -> StudentProfile.show(frame));
        applyButton.addActionListener(e -> {
            System.out.println("Navigating to PlacementApplication");
            try {
                PlacementApplication.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing PlacementApplication: " + ex.getMessage());
            }
        });
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
        System.out.println("StudentProfile frame updated");
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
