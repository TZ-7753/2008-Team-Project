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

public class AddToExistingOrderPanel extends JPanel{
    private String productCode;
    private Connection connection;
    private String customer_id;
    private AddToOrderPanel addToOrderPanel;

    private AddToExistingOrderEntriesPanel addToExistingOrderEntriesPanel;
    private AddToExistingOrderLabelsPanel addToExistingOrderLabelsPanel;

    public AddToExistingOrderPanel(String pC, Connection con, String user, AddToOrderPanel atop) {
        productCode = pC;
        connection = con;
        customer_id = user;
        addToOrderPanel = atop;

        setLayout(new GridLayout(0,2));
        List<String> orderList = new ArrayList<String>();
        DatabaseOperations databaseOperations = new DatabaseOperations();
        try {
            orderList = databaseOperations.getUserOrders(customer_id, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addToExistingOrderEntriesPanel = new AddToExistingOrderEntriesPanel(orderList);
        add(addToExistingOrderEntriesPanel);

        addToExistingOrderLabelsPanel = new AddToExistingOrderLabelsPanel(orderList, productCode, connection, customer_id, addToOrderPanel);
        add(addToExistingOrderLabelsPanel);
    }
}
