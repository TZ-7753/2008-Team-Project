package project.views;

import project.model.DatabaseOperations;
import project.util.HashedPasswordGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginView(Connection connection) throws SQLException {
        // Create the JFrame in the constructor
        this.setTitle("Login Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 150);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        this.add(panel);

        // Set a layout manager for the panel (e.g., GridLayout)
        panel.setLayout(new GridLayout(4, 2));

        // Create JLabels for username and password
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        // Create JTextFields for entering username and password
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Create a JButton for the login action
        JButton loginButton = new JButton("Login");

        JButton createAccountButton = new JButton("Create Account");

        // Add components to the panel
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);
        panel.add(new JLabel());
        panel.add(createAccountButton);

        // Create an ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                char[] passwordChars = passwordField.getPassword();
                String hashedPassword = HashedPasswordGenerator.hashPassword(passwordChars);

                DatabaseOperations databaseOperations = new DatabaseOperations();
                // Secure disposal of the password
                Arrays.fill(passwordChars, '\u0000');

                //Catch verification response
                ArrayList<String> response = databaseOperations.verifyLogin(connection, email, hashedPassword);

                //Verification Passed - close login view, open main screen
                if (response.get(0) == "success") {
                    // Open new window
                    try{
                        LoginView.this.dispose();
                        MainScreenView mainScreen = new MainScreenView(connection, response.get(1), response.get(2));
                        mainScreen.setVisible(true);
                    } catch (SQLException error){
                        error.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, response.get(1));
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    CreateAccountView createAccount = new CreateAccountView(connection);
                    createAccount.setVisible(true);
                } catch (SQLException error) {
                    error.printStackTrace();
                }

            }
        });
    }
}
