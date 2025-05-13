package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentDashboard {
    public static void show(JFrame frame) {
        System.out.println("Showing StudentDashboard");
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // Full-screen Background
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon("src/main/resources/bg.jpg").getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent overlay for readability
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Transparent to show background
        mainPanel.setBorder(new EmptyBorder(60, 60, 60, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JLabel headerLabel = new JLabel("Placement Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerLabel, gbc);

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setOpaque(false);
        JButton dashboardButton = new JButton("Dashboard");
        JButton profileButton = new JButton("Profile");
        JButton applyButton = new JButton("Apply");
        JButton trackButton = new JButton("Track Applications");
        JButton logoutButton = new JButton("Logout");
        styleButton(dashboardButton, "dashboard-icon.png");
        styleButton(profileButton, "profile-icon.png");
        styleButton(applyButton, "apply-icon.png");
        styleButton(trackButton, "track-icon.png");
        styleButton(logoutButton, "logout-icon.png");
        Dimension buttonSize = new Dimension(120, 40);
        dashboardButton.setPreferredSize(buttonSize);
        profileButton.setPreferredSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        trackButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);
        navPanel.add(dashboardButton);
        navPanel.add(profileButton);
        navPanel.add(applyButton);
        navPanel.add(trackButton);
        navPanel.add(logoutButton);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(navPanel, gbc);

        // Welcome Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints contentGbc = new GridBagConstraints();
        contentGbc.insets = new Insets(20, 20, 20, 20);
        contentGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome to Student Dashboard", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        contentGbc.gridx = 0;
        contentGbc.gridy = 0;
        contentGbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(welcomeLabel, contentGbc);

        JLabel imageLabel = new JLabel(new ImageIcon("src/main/resources/student.png"));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentGbc.gridy = 1;
        contentGbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(imageLabel, contentGbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(contentPanel, gbc);

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        dashboardButton.addActionListener(e -> StudentDashboard.show(frame));
        profileButton.addActionListener(e -> {
            System.out.println("Navigating to StudentProfile");
            try {
                StudentProfile.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing StudentProfile: " + ex.getMessage());
            }
        });
        applyButton.addActionListener(e -> {
            System.out.println("Navigating to PlacementApplication");
            try {
                PlacementApplication.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing PlacementApplication: " + ex.getMessage());
            }
        });
        trackButton.addActionListener(e -> {
            System.out.println("Navigating to StudentApplicationTracking");
            try {
                StudentApplicationTracking.show(frame);
            } catch (Exception ex) {
                System.out.println("Error showing StudentApplicationTracking: " + ex.getMessage());
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

        frame.setSize(1200, 800); // Ensure sufficient size for visibility
        frame.setLocationRelativeTo(null); // Center the frame
        frame.revalidate();
        frame.repaint();
        System.out.println("StudentDashboard frame updated");
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
