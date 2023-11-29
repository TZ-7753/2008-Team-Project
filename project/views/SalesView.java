package project.views;

import project.model.DatabaseOperations;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.math.BigDecimal;

public class SalesView extends JFrame {

    private JTable ordersTable;
    private DatabaseOperations dbOps;
    private JTextField filterByDateField;
    private JTextField filterByOrderNumField;
    private JButton filterByDateButton;
    private JButton filterByOrderNumButton;
    private JButton staffViewButton;
    private Connection connection;
    private String currentUserId;
    private String currentUserRole;

    public SalesView(Connection connection, String userId, String userRole) {
        this.currentUserId = userId;
        this.currentUserRole = userRole;
        this.connection = connection;
        this.dbOps = new DatabaseOperations();

        this.setTitle("Sales View");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());
        this.add(panel);

        ordersTable = new JTable();
        displayFulfilledOrders();

        filterByDateField = new JTextField(10);
        filterByOrderNumField = new JTextField(10);
        filterByDateButton = new JButton("Filter by Date");
        filterByOrderNumButton = new JButton("Filter by Order Number");
        staffViewButton = new JButton("Back to Staff View");

        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filter by Date:"));
        filterPanel.add(filterByDateField);
        filterPanel.add(filterByDateButton);
        filterPanel.add(new JLabel("Filter by Order Number:"));
        filterPanel.add(filterByOrderNumField);
        filterPanel.add(filterByOrderNumButton);

        panel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(staffViewButton, BorderLayout.SOUTH);

        filterByDateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterOrdersByDate();
            }
        });

        filterByOrderNumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterOrdersByOrderNumber();
            }
        });

        staffViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToStaffView(connection, userId, userRole);
            }
        });
    }

    private void displayFulfilledOrders() {
        try {
            String query = "SELECT * FROM orders WHERE order_status = 'Fulfilled'";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();
                DefaultTableModel model = new DefaultTableModel(new String[]{"Order Number", "Customer ID", "Order Status", "Order Date", "Total Cost"}, 0);
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getInt("order_number"), rs.getString("customer_ID"), rs.getString("order_status"), rs.getDate("order_date"), rs.getBigDecimal("totalCost")});
                }
                ordersTable.setModel(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterOrdersByDate() {
        try {
            String selectedDate = filterByDateField.getText().trim();
            if (selectedDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date to filter.");
                return;
            }
    
            String query = "SELECT * FROM orders WHERE order_status = 'Fulfilled' AND order_date = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setDate(1, java.sql.Date.valueOf(selectedDate)); // 将字符串转换为 sql.Date
                ResultSet rs = pstmt.executeQuery();
    
                String[] columnNames = {"Order Number", "Customer ID", "Order Status", "Order Date", "Total Cost"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    
                while (rs.next()) {
                    int orderNumber = rs.getInt("order_number");
                    String customerID = rs.getString("customer_ID");
                    String orderStatus = rs.getString("order_status");
                    Date orderDate = rs.getDate("order_date");
                    BigDecimal totalCost = rs.getBigDecimal("totalCost");
    
                    model.addRow(new Object[]{orderNumber, customerID, orderStatus, orderDate, totalCost});
                }
    
                ordersTable.setModel(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while filtering orders.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
        }
    }
    

    private void filterOrdersByOrderNumber() {
        try {
            String orderNumStr = filterByOrderNumField.getText().trim();
            if (orderNumStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an order number to filter.");
                return;
            }
    
            int orderNumber;
            try {
                orderNumber = Integer.parseInt(orderNumStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid order number format.");
                return;
            }
    
            String query = "SELECT * FROM orders WHERE order_status = 'Fulfilled' AND order_number = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, orderNumber);
                ResultSet rs = pstmt.executeQuery();
    
                String[] columnNames = {"Order Number", "Customer ID", "Order Status", "Order Date", "Total Cost"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    
                while (rs.next()) {
                    int orderNum = rs.getInt("order_number");
                    String customerID = rs.getString("customer_ID");
                    String orderStatus = rs.getString("order_status");
                    Date orderDate = rs.getDate("order_date");
                    BigDecimal totalCost = rs.getBigDecimal("totalCost");
    
                    model.addRow(new Object[]{orderNum, customerID, orderStatus, orderDate, totalCost});
                }
    
                ordersTable.setModel(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while filtering orders.");
        }
    }
    

    private void goToStaffView(Connection connection, String userId, String userRole) {
        this.dispose();
        try {
            StaffViewWindow staffView = new StaffViewWindow(connection, userId, userRole);
            staffView.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
