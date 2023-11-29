package project.views.customerproductsearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.StaffViewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;


public class CustomerProductDetailsWindow extends JFrame{
    private Connection connection;
    private String productCode;
    private Container contentPane;

    private ProductDetailsPanel productDetailsPanel;
    private AddToOrderPanel addToOrderPanel;
    private String customer_id;

    public CustomerProductDetailsWindow(String pC, Connection con, String category, String userID, String userRole) {
        productCode = pC;
        connection = con;

        customer_id = "2753e5f1-241e-421b-94f7-63a90a52787b";

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));

        productDetailsPanel = new ProductDetailsPanel(productCode, connection);
        contentPane.add(productDetailsPanel);

        addToOrderPanel = new AddToOrderPanel(productCode, connection, userID, productDetailsPanel);
        contentPane.add(addToOrderPanel);


        JMenu navigationBar = new JMenu("Nav");

        JMenuItem dv = new JMenuItem("Default View");
        navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

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
                }
            });
        }


        JMenuItem back = new JMenuItem("Back");
        navigationBar.addSeparator();
        navigationBar.add(back);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerProductSearchWindow cpsw = new CustomerProductSearchWindow(category,connection, userID, userRole);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }
}