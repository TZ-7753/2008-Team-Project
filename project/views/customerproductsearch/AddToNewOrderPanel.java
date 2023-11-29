package project.views.customerproductsearch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
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

public class AddToNewOrderPanel extends JPanel{
    private String productCode;
    private Connection connection;
    private String customer_id;
    private AddToOrderPanel addToOrderPanel;
    public AddToNewOrderPanel(String pC, Connection con, String user, AddToOrderPanel atop) {
        productCode = pC;
        connection = con;
        customer_id = user;
        addToOrderPanel = atop;

        JButton create_order = new JButton("Add To New Order");
        add(create_order);
        create_order.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if (command.equals("Add To New Order")) {
                    DatabaseOperations databaseOperations = new DatabaseOperations();
                    int quantity = addToOrderPanel.getProductDetailsPanel().getQuantity();
                    try {
                        databaseOperations.addToNewOrder(customer_id, productCode, quantity, connection);
                        JOptionPane.showMessageDialog(atop, "Successfully added to Order");
                    } catch (Exception event) {
                        event.printStackTrace();
                        JOptionPane.showMessageDialog(atop, "There was an error adding to Order");
                    }
                }
            }
        });
    }
}
