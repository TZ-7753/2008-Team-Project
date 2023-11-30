package project.views.customerproductsearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.inventory.InventoryProductSearchEntriesPanel;
import project.views.inventory.InventoryProductSearchLabelsPanel;
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
public class CustomerProductSearchEntriesPanel extends JPanel {

    public CustomerProductSearchEntriesPanel(List<String> productCodes, List<String> productNames) {
        setLayout(new GridLayout(0,1));
        for (int i = 0; i < productCodes.size(); i++) {
            add(new JTextField("Code:" + productCodes.get(i) + ";      Product Name:" + productNames.get(i)));
        }
    }
}
