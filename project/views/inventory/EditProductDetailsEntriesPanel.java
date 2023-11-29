package project.views.inventory;

import project.model.DatabaseOperations;
import project.model.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import java.sql.*;
public class EditProductDetailsEntriesPanel extends JPanel{
    private Connection connection;
    private List<JTextField> productTextFields = new ArrayList<JTextField>();
    private List<JTextField> productPartTextFields = new ArrayList<JTextField>();
    private JComboBox<String> controllerDigital = new JComboBox<String>();
    private List<JComboBox<Integer>> partQuantityBoxes = new ArrayList<JComboBox<Integer>>();
    private int newPartCount = 0;

    public List<JTextField> getProductTextFields() {
        return this.productTextFields;
    }

    public JComboBox<String> getControllerDigital() {
        return this.controllerDigital;
    }

    public List<JComboBox<Integer>> getPartQuantityBoxes() {
        return this.partQuantityBoxes;
    }

    public int getNewPartCount() {
        return this.newPartCount;
    }

    public List<JTextField> getProductPartTextFields() {
        return this.productPartTextFields;
    }

    public EditProductDetailsEntriesPanel(String productCode, int partCount, int newPartCount, Connection con) {
        connection = con;
        setLayout(new GridLayout(0,1));
        char productType = productCode.charAt(0);

        DatabaseOperations databaseOperations = new DatabaseOperations();
        List<String> current_details = new ArrayList<String>();
        try {
            current_details = databaseOperations.getProductDetails(productCode, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTextField productNameField = new JTextField(current_details.get(1));
        JTextField productBrandField = new JTextField(current_details.get(2));
        JTextField productRetailPriceField = new JTextField(current_details.get(3));
        JTextField productUKGaugeField = new JTextField(current_details.get(4));
        JTextField productStockField = new JTextField(current_details.get(5));

        add(productNameField);
        add(productBrandField);
        add(productRetailPriceField);
        add(productUKGaugeField);
        add(productStockField);

        productTextFields.add(productNameField);
        productTextFields.add(productBrandField);
        productTextFields.add(productRetailPriceField);
        productTextFields.add(productUKGaugeField);
        productTextFields.add(productStockField);

        JTextField eraCodeField;

        switch (productType) {
            case 'S':
                eraCodeField = new JTextField(current_details.get(6));
                add(eraCodeField);
                productTextFields.add(eraCodeField);
                break;
            case 'L':
                eraCodeField = new JTextField(current_details.get(6));
                JTextField dccCodeField = new JTextField(current_details.get(7));
                add(eraCodeField);
                add(dccCodeField);
                productTextFields.add(eraCodeField);
                productTextFields.add(dccCodeField);
                break;
            case 'C':
                controllerDigital = new JComboBox<String>();
                controllerDigital.addItem("yes");
                controllerDigital.addItem("no");
                controllerDigital.setSelectedItem(current_details.get(6));
                add(controllerDigital);
            default:
                break;
        }

        if ((productType == 'M') || (productType == 'P')) {
            for (int i = 0; i < partCount*2; i+= 2) {
                JTextField currentPart = new JTextField(current_details.get(6+i));
                JComboBox<Integer> currentPartQuantity = new JComboBox<Integer>();
                for (int x = 1; x <= 100; x ++) {
                    currentPartQuantity.addItem(x);
                }
                currentPartQuantity.setSelectedItem(Integer.valueOf(current_details.get(6+i+1)));
                productPartTextFields.add(currentPart);
                partQuantityBoxes.add(currentPartQuantity);
            }
            if (newPartCount > partCount) {
                for (int i = 0; i < newPartCount - partCount; i++) {
                    JTextField currentPart = new JTextField();
                    JComboBox<Integer> currentPartQuantity = new JComboBox<Integer>();
                    for (int x = 1; x <= 100; x ++) {
                        currentPartQuantity.addItem(x);
                    }
                    currentPartQuantity.setSelectedItem(1);
                    productPartTextFields.add(currentPart);
                    partQuantityBoxes.add(currentPartQuantity);
                }
            }

            for (int i = 0; i < newPartCount; i++) {
                add(productPartTextFields.get(i));
                add(partQuantityBoxes.get(i));
            }
        }

    }
}
