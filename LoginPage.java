package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.example.AdminCompanyManagement;
import org.example.CommitteeDashboard;
import org.example.StudentDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPage {
    private static JFrame frame;
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    static String currentUsername;
    static String currentRole;

    public static void show(JFrame parentFrame) {
        if (parentFrame == null) {
            frame = new JFrame("Placement Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame = parentFrame;
            frame.getContentPane().removeAll();
        }
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
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        final JLabel[] headerLabel = {new JLabel("Placement Management System", JLabel.CENTER)};
        headerLabel[0].setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel[0].setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel[0], gbc);

        JLabel logoLabel = new JLabel(new ImageIcon("src/main/resources/logo.png"));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 1;
        mainPanel.add(logoLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        mainPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, "login-icon.png");
        loginButton.setPreferredSize(new Dimension(120, 40)); // Set uniform size
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        JButton signInButton = new JButton("Sign In");
        styleButton(signInButton, "signin-icon.png");
        signInButton.setPreferredSize(new Dimension(120, 40)); // Set uniform size
        gbc.gridx = 1;
        mainPanel.add(signInButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("Attempting login with: " + username + ", " + email + ", " + password);

            if (mongoClient == null) {
                try {
                    mongoClient = MongoClients.create("mongodb://localhost:27017");
                    database = mongoClient.getDatabase("placement_db");
                    System.out.println("MongoDB connection established");
                } catch (Exception ex) {
                    System.out.println("MongoDB connection failed: " + ex.getMessage());
                    JOptionPane.showMessageDialog(frame, "MongoDB connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            MongoCollection<Document> users = database.getCollection("users");
            Document user = users.find(Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("email", email)
            )).first();
            System.out.println("User found: " + user);

            if (user != null && user.getString("password").equals(password)) {
                currentUsername = username;
                currentRole = user.getString("role");
                System.out.println("Login successful for role: " + currentRole);
                frame.getContentPane().removeAll();
                frame.setLayout(new BorderLayout());

                // Enhanced Header for Admin
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
                    }
                };
                headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 100));
                headerLabel[0] = new JLabel("Placement Management System - Admin: " + currentUsername, JLabel.CENTER);
                headerLabel[0].setFont(new Font("Arial", Font.BOLD, 24));
                headerLabel[0].setForeground(Color.WHITE);
                headerPanel.add(headerLabel[0], BorderLayout.CENTER);

                JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
                navPanel.setOpaque(false);
                JButton committeeBtn = new JButton("Committee Dashboard");
                JButton adminBtn = new JButton("Admin Companies");
                committeeBtn.setPreferredSize(new Dimension(180, 40));
                adminBtn.setPreferredSize(new Dimension(180, 40));
                styleHeaderButton(committeeBtn);
                styleHeaderButton(adminBtn);
                navPanel.add(committeeBtn);
                navPanel.add(adminBtn);
                headerPanel.add(navPanel, BorderLayout.SOUTH);

                frame.add(headerPanel, BorderLayout.NORTH);

                if ("student".equalsIgnoreCase(currentRole)) {
                    StudentDashboard.show(frame);
                } else if ("committee".equalsIgnoreCase(currentRole)) {
                    CommitteeDashboard.show(frame);
                } else if ("admin".equalsIgnoreCase(currentRole)) {
                    AdminCompanyManagement.show(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Unknown role", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signInButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("Attempting sign in with: " + username + ", " + email + ", " + password);

            if (mongoClient == null) {
                try {
                    mongoClient = MongoClients.create("mongodb://localhost:27017");
                    database = mongoClient.getDatabase("placement_db");
                    System.out.println("MongoDB connection established");
                } catch (Exception ex) {
                    System.out.println("MongoDB connection failed: " + ex.getMessage());
                    JOptionPane.showMessageDialog(frame, "MongoDB connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(frame, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            MongoCollection<Document> users = database.getCollection("users");

            // Check if username or email already exists
            Document existingUserByUsername = users.find(Filters.eq("username", username)).first();
            if (existingUserByUsername != null) {
                JOptionPane.showMessageDialog(frame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Document existingUserByEmail = users.find(Filters.eq("email", email)).first();
            if (existingUserByEmail != null) {
                JOptionPane.showMessageDialog(frame, "Email already registered", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(frame, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Document newUser = new Document()
                    .append("username", username)
                    .append("email", email)
                    .append("password", password)
                    .append("role", "student");
            users.insertOne(newUser);
            System.out.println("User signed in: " + username);
            JOptionPane.showMessageDialog(frame, "Sign in successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields after successful signup
            usernameField.setText("");
            emailField.setText("");
            passwordField.setText("");
        });

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        frame.setSize(1200, 800); // Set a default size
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
        System.out.println("Frame updated");
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    private static void styleButton(JButton button, String iconPath) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
            }
            @Override
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

    static void styleHeaderButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 90, 160));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });
    }
}
