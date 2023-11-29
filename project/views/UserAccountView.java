package project.views;

import project.model.DatabaseOperations;
import project.util.HashedPasswordGenerator;
import project.util.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class UserAccountView extends JFrame {
    private JTextField firstNameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField houseNumberField;
    private JTextField streetNameField;
    private JTextField cityNameField;
    private JTextField postcodeField;
    private JTextField cardNoField;
    private JTextField cardNameField;
    private JTextField cardExpiryDateField;
    private JTextField cardSecurityCodeField;

    public UserAccountView(Connection connection, String userID, String userRole) throws SQLException {
        // Create the JFrame in the constructor
        this.setTitle("View User Account");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 600);

        JMenu navigationBar = new JMenu("Menu");

        JMenuItem dv = new JMenuItem("Main Screen");
        navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    MainScreenView msv = new MainScreenView(connection, userID, userRole);
                    msv.setVisible(true);
                } catch (SQLException err) {
                    err.printStackTrace();
                }
            }
        });

        if (userRole.equals("Manager") || userRole.equals("Staff")) {
            JMenuItem sv = new JMenuItem("Staff View");
            navigationBar.addSeparator();
            navigationBar.add(sv);
            sv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    StaffViewWindow sfw = new StaffViewWindow(connection, userID, userRole);
                }
            });

        }

        if (userRole.equals("Manager")) {
            JMenuItem sr = new JMenuItem("Staff Registry");
            navigationBar.addSeparator();
            navigationBar.add(sr);
            sr.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        StaffRegistryView srv = new StaffRegistryView(connection, userID, userRole);
                        srv.setVisible(true);
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }
                }
            });
        }


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        this.add(panel);

        // Set a layout manager for the panel (e.g., GridLayout)
        panel.setLayout(new GridLayout(19, 2));

        DatabaseOperations databaseOperations = new DatabaseOperations();
        String[] userInfo = databaseOperations.getUserInfo(connection, userID);

        if (userInfo != null) {
            // Create JLabels
            JLabel firstNameLabel = new JLabel("Firstname:");
            JLabel surnameLabel = new JLabel("Surname:");
            JLabel emailLabel = new JLabel("Email:");
            JLabel passwordLabel = new JLabel("Password:");
            JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
            JLabel homeAddressLabel = new JLabel("Home Address:");
            JLabel houseNumberLabel = new JLabel("House Number:");
            JLabel streetNameLabel = new JLabel("Street Name:");
            JLabel cityNameLabel = new JLabel("City Name:");
            JLabel postcodeLabel = new JLabel("Postcode:");
            JLabel bankDetailsLabel = new JLabel("Bank Details:");
            JLabel cardNoLabel = new JLabel("Card Number:");
            JLabel cardNameLabel = new JLabel("Card Name:");
            JLabel cardExpiryDateLabel = new JLabel("Expiry Date (MM/YYYY):");
            JLabel cardSecurityCodeLabel = new JLabel("Security Code:");

            // Create JTextFields
            firstNameField = new JTextField(userInfo[0]);
            surnameField = new JTextField(userInfo[1]);
            emailField = new JTextField(userInfo[2]);
            passwordField = new JPasswordField(20);
            confirmPasswordField = new JPasswordField(20);
            houseNumberField = new JTextField(userInfo[3]);
            streetNameField = new JTextField(userInfo[4]);
            cityNameField = new JTextField(userInfo[5]);
            postcodeField = new JTextField(userInfo[6]);
            cardNoField = new JTextField(16);
            cardNameField = new JTextField(40);
            cardExpiryDateField = new JTextField(20);
            cardSecurityCodeField = new JTextField(3);

            // Create a JButton for the login action
            JButton updateDetailsButton = new JButton("Update Details");

            // Create a JButton for the create account action
            JButton backButton = new JButton("Go Back");

            // Add components to the panel
            panel.add(firstNameLabel);
            panel.add(firstNameField);

            panel.add(surnameLabel);
            panel.add(surnameField);

            panel.add(emailLabel);
            panel.add(emailField);

            panel.add(passwordLabel);
            panel.add(passwordField);

            panel.add(confirmPasswordLabel);
            panel.add(confirmPasswordField);

            panel.add(new JLabel());
            panel.add(new JLabel());

            panel.add(homeAddressLabel);
            panel.add(new JLabel());

            panel.add(houseNumberLabel);
            panel.add(houseNumberField);

            panel.add(streetNameLabel);
            panel.add(streetNameField);

            panel.add(cityNameLabel);
            panel.add(cityNameField);

            panel.add(postcodeLabel);
            panel.add(postcodeField);

            panel.add(new JLabel());
            panel.add(new JLabel());

            panel.add(bankDetailsLabel);
            panel.add(new JLabel());

            panel.add(cardNoLabel);
            panel.add(cardNoField);

            panel.add(cardNameLabel);
            panel.add(cardNameField);

            panel.add(cardExpiryDateLabel);
            panel.add(cardExpiryDateField);

            panel.add(cardSecurityCodeLabel);
            panel.add(cardSecurityCodeField);

            panel.add(new JLabel());
            panel.add(updateDetailsButton);
            panel.add(new JLabel()); // Empty label for spacing
            panel.add(backButton);

            // Create an ActionListener for the create account button
            updateDetailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Boolean validForm = true;
                    String[] newUserInfo = { firstNameField.getText(), surnameField.getText(), emailField.getText(),
                            houseNumberField.getText(), streetNameField.getText(), cityNameField.getText(),
                            postcodeField.getText() };
                    String[] newBankDetails = { (cardNoField.getText()).replaceAll("\\s+", ""), cardNameField.getText(),
                            cardExpiryDateField.getText(), cardSecurityCodeField.getText() };
                    char[] passwordChars = passwordField.getPassword();
                    char[] confirmPasswordChars = confirmPasswordField.getPassword();

                    // form validation for empty fields and email
                    InputValidator inputValidator = new InputValidator();
                    if (newUserInfo[0].isEmpty() || newUserInfo[1].isEmpty() || newUserInfo[3].isEmpty()
                            || newUserInfo[4].isEmpty() || newUserInfo[5].isEmpty() || newUserInfo[6].isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Empty Input Values!");
                        validForm = false;
                    } else if (!inputValidator.validateEmail(newUserInfo[2])) {
                        JOptionPane.showMessageDialog(null, "Invalid Email!");
                        validForm = false;
                    }

                    // validation and update for password field
                    if (passwordChars.length != 0) {
                        if (!inputValidator.validatePassword(passwordChars)) {
                            JOptionPane.showMessageDialog(null,
                                    "Password must be at lease 8 characters in length, contain a capital letter and contain a numeric value!");
                            validForm = false;
                        } else {
                            if (Arrays.equals(passwordChars, confirmPasswordChars)) {
                                String hashedPassword = HashedPasswordGenerator.hashPassword(passwordChars);
                                DatabaseOperations databaseOperations = new DatabaseOperations();
                                databaseOperations.updatePassword(connection, userID, hashedPassword);
                            } else {
                                JOptionPane.showMessageDialog(null, "Inputted passwords do not match!");
                                validForm = false;
                            }
                        }
                    }

                    if (!(newBankDetails[0].length() == 0 && newBankDetails[1].length() == 0
                            && newBankDetails[2].length() == 0 && newBankDetails[3].length() == 0)) {
                        if (!(newBankDetails[0].length() == 0 || newBankDetails[1].length() == 0
                                || newBankDetails[2].length() == 0 || newBankDetails[3].length() == 0)) {
                            if (inputValidator.validateCardNumber(newBankDetails[0])) {
                                if (inputValidator.validateDate(newBankDetails[2])) {
                                    if (inputValidator.validateSecurityCode(newBankDetails[3])) {
                                        databaseOperations.updateBankDetails(connection, userID, newBankDetails);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Invalid Security Code!");
                                        validForm = false;
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid Expiry Date!");
                                    validForm = false;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Card Number");
                                validForm = false;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "New Bank Details are incomplete!");
                            validForm = false;
                        }
                    }

                    // updates rest of fields
                    if (!Arrays.equals(userInfo, newUserInfo)) {
                        DatabaseOperations databaseOperations = new DatabaseOperations();
                        databaseOperations.updateUserInfo(connection, userID, newUserInfo);
                    }

                    if (validForm) {
                        try {
                            dispose();
                            MainScreenView mainscr = new MainScreenView(connection, userID, userRole);
                            mainscr.setVisible(true);
                        } catch (SQLException error) {
                            error.printStackTrace();
                        }
                    }
                }
            });

            // Create an ActionListener for the login button
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        MainScreenView mainscr = new MainScreenView(connection, userID, userRole);
                        mainscr.setVisible(true);
                    } catch (SQLException error) {
                        error.printStackTrace();
                    }
                }
            });
        }
    }
}
