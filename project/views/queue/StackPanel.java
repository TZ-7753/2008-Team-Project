package project.views.queue;

import project.views.queue.QueueWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.inventory.InventoryProductSearchWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class StackPanel extends JPanel {

    private QueueWindow queuewindow;
    private Connection connection;
    public StackPanel(QueueWindow queue, Connection con) {
        queuewindow = queue;
        connection = con;
        setLayout(new CardLayout());
        JTextArea orderInfo = new JTextArea();

        List<String> orderDetails= new ArrayList<String>();
        DatabaseOperations databaseOperations = new DatabaseOperations();
        try {
            orderDetails = databaseOperations.retrieveOrderQueue(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (orderDetails.size() != 0) {
            List<String> titles = new ArrayList<String>();

            titles.add("Order Number: ");
            titles.add("Customer_ID: ");
            titles.add("Order Date: ");
            titles.add("Total Cost: ");
            titles.add("First name: ");
            titles.add("Last Name: ");
            titles.add("Email: ");
            titles.add("House Number: ");
            titles.add("Postcode: ");
            titles.add("Road Name: ");
            titles.add("City Name: ");
            titles.add("Order Line Number: ");
            titles.add("Product Code: ");
            titles.add("Quantity: ");
            titles.add("Line Cost: ");

            for (int i = 0; i < 11; i++) {
                orderInfo.append(titles.get(i));
                orderInfo.append(orderDetails.get(i));
                orderInfo.append(System.lineSeparator());
            }

            int order_lines = (orderDetails.size() - 11) / 4;
            for (int i = 0; i < order_lines; i++) {
                for (int x = 0; x < 4; x++) {
                    orderInfo.append(titles.get(11 + x));
                    orderInfo.append(orderDetails.get(i * 4 + x + 11));
                    orderInfo.append(System.lineSeparator());
                }
            }
        } else {
            orderInfo.setText("Nothing in the Queue");
        }
        orderInfo.setEditable(false);
        add(orderInfo);

    }
}
