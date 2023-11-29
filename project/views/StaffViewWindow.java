package project.views;

import project.views.inventory.InventoryWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.inventory.InventoryProductSearchResultsPanel;
import project.views.inventory.InventoryProductSearchWindow;
import project.views.inventory.NewProductWindow;
import project.views.queue.QueueWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class StaffViewWindow extends JFrame{
    private static final long serialVersionUID = 2L;

    public StaffViewWindow(Connection connection, String userID, String userRole) throws HeadlessException {
        setTitle("Staff View Main Screen");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1,0));

        JButton queue = new JButton("Queue");
        JButton inventory = new JButton("Inventory");
        JButton sales = new JButton("Sales");
        contentPane.add(queue);
        contentPane.add(inventory);
        contentPane.add(sales);

        queue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                QueueWindow iw = new QueueWindow(connection, userID, userRole);
            }
        });

        inventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InventoryWindow iw = new InventoryWindow(connection, userID, userRole);
            }
        });

        sales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SalesView sv = new SalesView(connection, userID, userRole);
                sv.setVisible(true);
            }
        });

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

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();
            StaffViewWindow npr = new StaffViewWindow(databaseConnectionHandler.getConnection(), "71368249-8571-442d-ad01-1723829da082", "Manager");

        }catch (Throwable t) {
            databaseConnectionHandler.closeConnection();
            throw new RuntimeException(t);
        }
    }
}
