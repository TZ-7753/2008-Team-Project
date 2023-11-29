package project.views.customerproductsearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
public class AddToOrderPanel extends JPanel{

    private String productCode;
    private Connection connection;
    private String customer_id;
    private ProductDetailsPanel productDetailsPanel;

    private AddToNewOrderPanel addToNewOrderPanel;

    private AddToExistingOrderPanel addToExistingOrderPanel;

    public ProductDetailsPanel getProductDetailsPanel() {
        return productDetailsPanel;
    }

    public AddToOrderPanel(String pC, Connection con, String user, ProductDetailsPanel pdp) {
        productCode = pC;
        connection = con;
        customer_id = user;
        productDetailsPanel = pdp;

        setLayout(new BorderLayout());

        addToExistingOrderPanel = new AddToExistingOrderPanel(productCode, connection, customer_id, this);
        addToNewOrderPanel = new AddToNewOrderPanel(productCode, connection, customer_id, this);

        add(addToExistingOrderPanel, BorderLayout.CENTER);
        add(addToNewOrderPanel, BorderLayout.SOUTH);
    }
}
