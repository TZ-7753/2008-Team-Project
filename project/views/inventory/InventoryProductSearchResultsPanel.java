package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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


public class InventoryProductSearchResultsPanel extends JPanel{
    private Connection connection;
    private String category;
    public InventoryProductSearchResultsPanel(String option, Connection con, InventoryProductSearchWindow ipsw) {
        category = option;
        connection = con;

        List<JTextField> productList = new ArrayList<JTextField>();
        List<String> productNames = new ArrayList<String>();
        List<String> productCodes = new ArrayList<String>();

        DatabaseOperations databaseOperations = new DatabaseOperations();
        List<String> results = new ArrayList<String>();
        try {
            results = databaseOperations.productSearch(category, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int count = 0;
        for (String s : results) {
            if ((count % 2) == 0) {
                productCodes.add(s);
            } else {
                productNames.add(s);
            }
            count += 1;
        }

        setLayout(new BorderLayout());
        add(new InventoryProductSearchLabelsPanel(productCodes, connection, ipsw), BorderLayout.WEST);
        add(new InventoryProductSearchEntriesPanel(productCodes, productNames), BorderLayout.CENTER);
    }
}
