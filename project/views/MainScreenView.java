package project.views;

import project.views.customerproductsearch.CustomerProductSearchWindow;

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

        /**
         * Creating Navigation menu. ---------------------------------------------------------------------------------
         */
        JMenu navigationBar = new JMenu("Menu");

        JMenuItem dv = new JMenuItem("Main Screen");
        //navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreenView.this.dispose();
                try{
                    MainScreenView.this.dispose();
                    MainScreenView mainScreenView = new MainScreenView(connection,userID,userRole);
                    mainScreenView.setVisible(true);
                }catch (SQLException err){
                    err.printStackTrace();
                }
            }
        });

        if (userRole.equals("Manager") || userRole.equals("Staff")) {
            JMenuItem sv = new JMenuItem("Staff View");
            //navigationBar.addSeparator();
            navigationBar.add(sv);
            sv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainScreenView.this.dispose();
                    StaffViewWindow sfw = new StaffViewWindow(connection, userID, userRole);
                    sfw.setVisible(true);
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
                    try{
                        MainScreenView.this.dispose();
                        StaffRegistryView staffRegistryView = new StaffRegistryView(connection,userID,userRole);
                        staffRegistryView.setVisible(true);
                    } catch (SQLException err){
                        err.printStackTrace();
                    }
                }
            });
        }
        /**
         * --------------------------------------------------------------------------------------------------------
         */

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
                dispose();
                CustomerProductSearchWindow rollingView = new CustomerProductSearchWindow("rolling stock",connection,userID,userRole);
            }
        });
        controllerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerProductSearchWindow rollingView = new CustomerProductSearchWindow("controller",connection,userID,userRole);
            }
        });
        locomotiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerProductSearchWindow rollingView = new CustomerProductSearchWindow("locomotive",connection,userID,userRole);
            }
        });
        trackPackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerProductSearchWindow rollingView = new CustomerProductSearchWindow("track pack",connection,userID,userRole);
            }
        });
        trainSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerProductSearchWindow rollingView = new CustomerProductSearchWindow("train set",connection,userID,userRole);
            }
        });
        trackPartsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerProductSearchWindow rollingView = new CustomerProductSearchWindow("train set",connection,userID,userRole);
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
                dispose();
                OrdersView ordersView = new OrdersView(connection,userID,userRole);
                ordersView.setVisible(true);
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

    }
}
