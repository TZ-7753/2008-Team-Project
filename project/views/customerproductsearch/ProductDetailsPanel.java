package project.views.customerproductsearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class ProductDetailsPanel extends JPanel{

    private String productCode;
    private Connection connection;

    private JComboBox<Integer> quantitySelected;

    public int getQuantity() {
        return (int) quantitySelected.getSelectedItem();
    }

    public ProductDetailsPanel(String pC, Connection con) {
        productCode = pC;
        connection = con;

        setLayout(new BorderLayout());

        List<String> results = new ArrayList<String>();
        DatabaseOperations databaseOperations = new DatabaseOperations();

        try {
            results = databaseOperations.getProductDetails(productCode, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> titles = new ArrayList<String>();
        titles.add("product code: ");
        titles.add("product name: ");
        titles.add("brand: ");
        titles.add("retail price: ");
        titles.add("uk_gauge: ");
        titles.add("stock: ");

        char productType = productCode.charAt(0);
        boolean boxedSet = false;

        switch (productType) {
            case 'R':
                break;
            case 'S':
                titles.add("era code: ");
                break;
            case 'L':
                titles.add("era code: ");
                titles.add("dcc code: ");
                break;
            case 'C':
                titles.add("is Digital?: ");
                break;
            default:
                titles.add("part code: ");
                titles.add("part quantity: ");
                boxedSet = true;
                break;
        }
        JTextArea outputInfo = new JTextArea();

        if (!boxedSet) {
            for (int i = 0; i < results.size(); i++) {
                outputInfo.append(titles.get(i));
                outputInfo.append(results.get(i));
                outputInfo.append(System.lineSeparator());
            }
        } else {
            for (int i = 0; i < 6; i++) {
                outputInfo.append(titles.get(i));
                outputInfo.append(results.get(i));
                outputInfo.append(System.lineSeparator());
            }
            for (int i = 0; i < (results.size() - 6)/2; i++) {
                for (int x = 0; x < 2; x ++) {
                    outputInfo.append(titles.get(x+6));
                    outputInfo.append(results.get(6+x+(i*2)));
                    outputInfo.append(System.lineSeparator());
                }
            }
        }
        outputInfo.append(System.lineSeparator());
        outputInfo.append(System.lineSeparator());

        outputInfo.append("Use the Box below to select the quantity to add to order.");
        add(outputInfo, BorderLayout.CENTER);
        outputInfo.setEditable(false);

        quantitySelected = new JComboBox<Integer>();
        for (int i = 1; i <= 100; i ++) {
            quantitySelected.addItem(i);
        }
        quantitySelected.setSelectedItem(1);
        add(quantitySelected, BorderLayout.SOUTH);
    }
}
