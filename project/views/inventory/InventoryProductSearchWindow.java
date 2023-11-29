package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.MainScreenView;
import project.views.StaffRegistryView;
import project.views.StaffViewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;



public class InventoryProductSearchWindow extends JFrame implements ActionListener{
    private JComboBox<String> productOptions;
    private String itemSelected;
    private Container contentPane;
    private InventoryProductSearchResultsPanel inventorySearchResults;
    private String userID;
    private String userRole;
    private Connection connection;
    public String getUserID() {
        return this.userID;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public InventoryProductSearchWindow(Connection con, String userI, String userR) throws HeadlessException{

        userID = userI;
        userRole = userR;
        connection = con;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        productOptions = new JComboBox<String>();
        productOptions.addItem("track piece");
        productOptions.addItem("rolling stock");
        productOptions.addItem("locomotive");
        productOptions.addItem("controller");
        productOptions.addItem("track pack");
        productOptions.addItem("train set");
        productOptions.setSelectedItem("track piece");
        productOptions.addActionListener(this);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(productOptions , BorderLayout.NORTH);

        itemSelected = productOptions.getSelectedItem().toString();

        inventorySearchResults = new InventoryProductSearchResultsPanel(itemSelected, connection, this);
        contentPane.add(inventorySearchResults, BorderLayout.CENTER);

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


        navigationBar.addSeparator();
        JMenuItem iw = new JMenuItem("Back");
        navigationBar.add(iw);

        iw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InventoryWindow iv = new InventoryWindow(connection, userID, userRole);
            }
        });


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        if (!(itemSelected.equals(productOptions.getSelectedItem().toString()))) {
            contentPane.remove(inventorySearchResults);
            itemSelected = productOptions.getSelectedItem().toString();
            inventorySearchResults = new InventoryProductSearchResultsPanel(itemSelected,connection, this);
            contentPane.add(inventorySearchResults, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();
            InventoryProductSearchWindow npr = new InventoryProductSearchWindow(databaseConnectionHandler.getConnection(), "werwer", "Manager");
            npr.setTitle("Bababooey");

        }catch (Throwable t) {
            databaseConnectionHandler.closeConnection();
            throw new RuntimeException(t);
        }
    }
}
