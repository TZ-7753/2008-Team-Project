package project.views;

import project.model.DatabaseOperations;
import project.util.HashedPasswordGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class MainScreenView extends JFrame {

    public MainScreenView(Connection connection) throws SQLException {
        // Create the JFrame in the constructor
        this.setTitle("Home");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        this.add(panel);

        // Set a layout manager for the panel (e.g., GridLayout)
        panel.setLayout(new GridLayout(3, 3));

        //Create the title
        JLabel titleLabel = new JLabel("Homepage - navigation");

        // Create JButton for the item types

        JButton controllerButton = new JButton("Controllers");
        JButton locomotiveButton = new JButton("Locomotives");
        JButton rollingButton = new JButton("Rolling Stock");
        JButton trackPackButton = new JButton("Track packs");
        JButton trackPartsButton = new JButton("Track parts");
        JButton trainSetButton = new JButton("Train Sets");

        panel.add(new JLabel());
        panel.add(titleLabel);
        panel.add(new JLabel());

        panel.add(controllerButton);
        panel.add(locomotiveButton);
        panel.add(rollingButton);
        panel.add(trackPackButton);
        panel.add(trackPartsButton);
        panel.add(trainSetButton);

        // Create listeners for each navigation buttons
        rollingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Rolling Stocks Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
        controllerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Controller Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
        locomotiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Locomotive Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
        trackPackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Track Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
        trainSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Train Set Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
        trackPartsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Track parts Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
    }
}
