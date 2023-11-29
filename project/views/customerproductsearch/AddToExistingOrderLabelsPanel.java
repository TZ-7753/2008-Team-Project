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

public class AddToExistingOrderLabelsPanel extends JPanel{
    private List<String> orderDetails;
    private String productCode;
    private Connection connection;
    private String customer_id;
    private AddToOrderPanel addToOrderPanel;
    public AddToExistingOrderLabelsPanel(List<String> ordL, String pC, Connection con, String user, AddToOrderPanel atop) {
        orderDetails = ordL;
        productCode = pC;
        connection = con;
        customer_id = user;
        addToOrderPanel = atop;

        setLayout(new GridLayout(0, 1));

        for (int i = 0; i < orderDetails.size(); i += 2) {
            JButton current = new JButton("Add to Order");
            add(current);
            int choice = i;
            current.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DatabaseOperations databaseOperations = new DatabaseOperations();
                    try {
                        int quantity = addToOrderPanel.getProductDetailsPanel().getQuantity();
                        databaseOperations.addToOrder(Integer.parseInt(orderDetails.get(choice)),productCode, quantity,connection);
                        JOptionPane.showMessageDialog(atop, "Successfully added to Order");
                    } catch (SQLException event) {
                        event.printStackTrace();
                        JOptionPane.showMessageDialog(atop, "There was an error adding to Order");
                    }
                }
            });
        }
    }
}
