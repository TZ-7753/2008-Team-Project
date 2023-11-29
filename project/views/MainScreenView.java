package project.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class MainScreenView extends JFrame {

    public MainScreenView(Connection connection, String userID, String userRole) throws SQLException {
        // Create the JFrame in the constructor
        this.setTitle("Home");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        this.add(panel);

        // Set a layout manager for the panel (e.g., GridLayout)
        panel.setLayout(new GridLayout(4, 3));

        //Create the title
        JLabel titleLabel = new JLabel("Homepage - navigation");

        //Create Nav bar
        JMenu navigationBar = new JMenu("Quick functions");
        JMenuItem staffRegistryItem = new JMenuItem("Staff Registry (Only for managers)");
        JMenuItem staffViewItem = new JMenuItem("Staff View (Only for staff)");
        JMenuItem mainScreenItem = new JMenuItem("Main Screen");
        navigationBar.add(staffRegistryItem);
        navigationBar.addSeparator();
        navigationBar.add(staffViewItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        // Create JButton for the item types
        JButton controllerButton = new JButton("Controllers");
        JButton locomotiveButton = new JButton("Locomotives");
        JButton rollingButton = new JButton("Rolling Stock");
        JButton trackPackButton = new JButton("Track packs");
        JButton trackPartsButton = new JButton("Track parts");
        JButton trainSetButton = new JButton("Train Sets");

        JButton accountViewButton = new JButton("Account View");
        JButton signOutButton = new JButton("Sign Out");
        JButton orderButton = new JButton("Orders");

        panel.add(new JLabel());
        panel.add(titleLabel);
        panel.add(new JLabel());

        panel.add(controllerButton);
        panel.add(locomotiveButton);
        panel.add(rollingButton);
        panel.add(trackPackButton);
        panel.add(trackPartsButton);
        panel.add(trainSetButton);

        panel.add(accountViewButton);
        panel.add(orderButton);
        panel.add(signOutButton);

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
        accountViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    MainScreenView.this.dispose();
                    UserAccountView account = new UserAccountView(connection,userID,userRole);
                    account.setVisible(true);
                }catch (SQLException err){
                    err.printStackTrace();
                }
            }
        });
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Order Page...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String dummyMsg = "Signing out.";
                    JOptionPane.showMessageDialog(null, dummyMsg);
                    dispose();
                    LoginView loginView = new LoginView(connection);
                    loginView.setVisible(true);
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });

        //Navbar Items
        staffRegistryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    MainScreenView.this.dispose();
                    StaffRegistryView staffRegistryView = new StaffRegistryView(connection,userID,userRole);
                    staffRegistryView.setVisible(true);
                }catch (SQLException err){
                    err.printStackTrace();
                }
            }
        });

        staffViewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dummyMsg = "Navigate to Staff View...";
                JOptionPane.showMessageDialog(null, dummyMsg);
            }
        });

        mainScreenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    MainScreenView.this.dispose();
                    MainScreenView mainScreenView = new MainScreenView(connection,userID,userRole);
                    mainScreenView.setVisible(true);
                }catch (SQLException err){
                    err.printStackTrace();
                }
            }
        });
    }
}
