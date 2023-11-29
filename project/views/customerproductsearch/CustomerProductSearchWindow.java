package project.views.customerproductsearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.MainScreenView;
import project.views.StaffRegistryView;
import project.views.StaffViewWindow;
import project.views.inventory.InventoryProductSearchResultsPanel;
import project.views.inventory.InventoryProductSearchWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class CustomerProductSearchWindow extends JFrame{
    private Connection connection;
    private String category;
    private CustomerSearchResultsPanel customerSearchResults;
    private String userID;
    private String userRole;
    public String getUserRole() {
        return this.userRole;
    }
    public String getUserID() {
        return this.userID;
    }
    public  CustomerProductSearchWindow(String cat, Connection con, String userI, String userR){

        userID = userI;
        userRole = userR;
        connection = con;
        category = cat;

        this.setTitle("All items of " + cat);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        customerSearchResults = new CustomerSearchResultsPanel(category, connection, this);
        contentPane.add(customerSearchResults, BorderLayout.CENTER);

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
                    try{
                        dispose();
                        StaffRegistryView staffRegistryView = new StaffRegistryView(connection,userID,userRole);
                        staffRegistryView.setVisible(true);
                    } catch (SQLException err){
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
            String category = "track piece";
            CustomerProductSearchWindow npr = new CustomerProductSearchWindow("track piece", databaseConnectionHandler.getConnection(), "werwer", "Manager");
            npr.setTitle("Bababooey");

        }catch (Throwable t) {
            databaseConnectionHandler.closeConnection();
            throw new RuntimeException(t);
        }
    }


}
