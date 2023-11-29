package project.views;

import project.model.DatabaseOperations;
import project.util.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class AddBankDetailsView extends JFrame {

    private JTextField cardNoField;
    private JTextField cardNameField;
    private JTextField cardExpiryDateField;
    private JTextField cardSecurityCodeField;

    public AddBankDetailsView(Connection connection, String userID, String userRole) throws SQLException {

        this.setTitle("Add Bank Details");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 600);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        this.add(panel);

        // Set a layout manager for the panel (e.g., GridLayout)
        panel.setLayout(new GridLayout(6, 2));

        JLabel cardNoLabel = new JLabel("Card Number:");
        JLabel cardNameLabel = new JLabel("Card Name:");
        JLabel cardExpiryDateLabel = new JLabel("Expiry Date (MM/YYYY):");
        JLabel cardSecurityCodeLabel = new JLabel("Security Code:");

        cardNoField = new JTextField(16);
        cardNameField = new JTextField(40);
        cardExpiryDateField = new JTextField(20);
        cardSecurityCodeField = new JTextField(3);

        JButton updateDetailsButton = new JButton("Add new details!");
        JButton backButton = new JButton("Go Back");

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

        updateDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean validForm = true;
                String[] newBankDetails = { (cardNoField.getText()).replaceAll("\\s+", ""), cardNameField.getText(),
                        cardExpiryDateField.getText(), cardSecurityCodeField.getText() };

                InputValidator inputValidator = new InputValidator();
                DatabaseOperations databaseOperations = new DatabaseOperations();

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

                if (validForm) {
                    try {
                        dispose();
                        UserAccountView mainscr = new UserAccountView(connection, userID, userRole);
                        mainscr.setVisible(true);
                    } catch (SQLException error) {
                        error.printStackTrace();
                    }
                }
            }
        });

    }
}

