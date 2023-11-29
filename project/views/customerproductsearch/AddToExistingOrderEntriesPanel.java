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

public class AddToExistingOrderEntriesPanel extends JPanel{
    public AddToExistingOrderEntriesPanel(List<String> orderList) {

        setLayout(new GridLayout(0,1));
        for (int i = 0; i < orderList.size(); i += 2) {
            JTextField current = new JTextField();
            current.setText("Order Number: " + orderList.get(i) + "   Total Cost: " + orderList.get(i+1));
            add(current);
        }
    }
}
