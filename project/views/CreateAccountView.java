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

public class CreateAccountView extends JFrame {
    private JTextField firstNameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField houseNumberField;
    private JTextField streetNameField;
    private JTextField cityNameField;
    private JTextField postcodeField;

    public CreateAccountView(Connection connection) throws SQLException {
        // Create the JFrame in the constructor
        this.setTitle("Create Account Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        this.add(panel);

        // Set a layout manager for the panel (e.g., GridLayout)
        panel.setLayout(new GridLayout(13, 2));

        // Create JLabels
        JLabel firstNameLabel = new JLabel("Firstname:");
        JLabel surnameLabel = new JLabel("Surname:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JLabel homeAddressLabel = new JLabel("Home Address:");
        JLabel houseNumberLabel = new JLabel("House Number:");
        JLabel streetNameLabel = new JLabel("Street Name:");
        JLabel cityNameLabel = new JLabel("City Name");
        JLabel postcodeLabel = new JLabel("Postcode:");

        // Create JTextFields
        firstNameField = new JTextField(20);
        surnameField = new JTextField(20);
        emailField = new JTextField(320);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        houseNumberField = new JTextField(20);
        streetNameField = new JTextField(20);
        cityNameField = new JTextField(20);
        postcodeField = new JTextField(20);

        // Create a JButton for the login action
        JButton loginButton = new JButton("Login");

        // Create a JButton for the create account action
        JButton createAccountButton = new JButton("Create Account");

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
        panel.add(createAccountButton);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);

        // Create an ActionListener for the create account button
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String surname = surnameField.getText();
                String email = emailField.getText();
                char[] passwordChars = passwordField.getPassword();
                char[] confirmPasswordChars = confirmPasswordField.getPassword();
                String houseNumber = houseNumberField.getText();
                String streetName = streetNameField.getText();
                String cityName = cityNameField.getText();
                String postcode = postcodeField.getText();

                Boolean validFormInput = true;
                InputValidator inputValidator = new InputValidator();
                if (firstName.isEmpty()||surname.isEmpty()||houseNumber.isEmpty()||streetName.isEmpty()||cityName.isEmpty()||postcode.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Empty Input Values!");
                    validFormInput = false;
                } else if(!inputValidator.validateEmail(email)) {
                    JOptionPane.showMessageDialog(null, "Invalid Email!");
                    validFormInput = false;
                } else if(!inputValidator.validatePassword(passwordChars)){
                    JOptionPane.showMessageDialog(null, "Password must be at lease 8 characters in length, contain a capital letter and contain a numeric value!");
                    validFormInput = false;
                }

                if(validFormInput){
                    if (Arrays.equals(passwordChars, confirmPasswordChars)) {
                        String hashedPassword = HashedPasswordGenerator.hashPassword(passwordChars);

                        DatabaseOperations databaseOperations = new DatabaseOperations();
                        databaseOperations.verifyAccountCreation(connection, firstName,
                                surname, email, hashedPassword, houseNumber, streetName, cityName, postcode);
                        try {
                            dispose();
                            LoginView login = new LoginView(connection);
                            login.setVisible(true);
                        } catch (SQLException error) {
                            error.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Inputted passwords do not match!");
                    }
                }
            }
        });

        // Create an ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    LoginView login = new LoginView(connection);
                    login.setVisible(true);
                } catch (SQLException error) {
                    error.printStackTrace();
                }

            }
        });
    }
}
