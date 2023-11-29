package project.views.queue;

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

public class DecisionPanel extends JPanel implements ActionListener{
    private QueueWindow queuewindow;
    private Connection connection;
    private String userID;
    private String userRole;
    private QueueWindow quw;
    public DecisionPanel(QueueWindow queue, Connection con, String uID, String uR, QueueWindow qw) {
        queuewindow = queue;
        connection = con;
        userID = uID;
        userRole = uR;
        quw = qw;

        setLayout(new FlowLayout());


        JButton confirm = new JButton("confirm");
        add(confirm);
        confirm.addActionListener(this);

        JButton decline = new JButton("decline");
        add(decline);
        decline.addActionListener(this);

        DatabaseOperations databaseOperations = new DatabaseOperations();
        boolean available = false;

        try {
            if (databaseOperations.inStock(connection)) {
                available = true;
            } else { available = false;}
        } catch (SQLException e) {
            e.printStackTrace();
        }

        confirm.setEnabled(available);


    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        DatabaseOperations databaseOperations = new DatabaseOperations();
        if (command.equals("confirm")) {
            try {
                databaseOperations.fulfillOrder(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            quw.dispose();
            QueueWindow npr = new QueueWindow(connection, userID, userRole);
            npr.setTitle("Bababooey");
        } else if (command.equals("decline")) {
            try {
                databaseOperations.staffDeleteQueueOrder(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            quw.dispose();
            QueueWindow npr = new QueueWindow(connection, userID, userRole);
            npr.setTitle("Bababooey");
        }
    }
}
