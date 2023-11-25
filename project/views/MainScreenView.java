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
        JButton carriageButton = new JButton("Carriage");
        JButton controllerButton = new JButton("Controllers");
        JButton locomotiveButton = new JButton("Locomotives");
        JButton trackPackButton = new JButton("Track packs");
        JButton trainSetButton = new JButton("Train Sets");
        JButton wagonButton = new JButton("Wagons");

        panel.add(new JLabel());
        panel.add(titleLabel);
        panel.add(new JLabel());

        panel.add(carriageButton);
        panel.add(controllerButton);
        panel.add(locomotiveButton);
        panel.add(trackPackButton);
        panel.add(trainSetButton);
        panel.add(wagonButton);

        // Create listeners for each navigation buttons
        carriageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Carriage Page...";
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
        wagonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Wagon Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
    }
}
