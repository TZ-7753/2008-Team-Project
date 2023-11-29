package project.views;

import project.model.DatabaseOperations;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.math.BigDecimal;
import java.sql.SQLException;

public class OrdersView extends JFrame {

    private JTable ordersTable;
    private DatabaseOperations dbOps;
    private JButton deleteButton;
    private JButton deleteOrderLineButton;
    private JButton editButton;
    private JButton fulfilledOrdersButton;
    private JButton confirmedOrdersButton;
    private JButton pendingOrdersButton;
    private JButton payButton;
    private JButton homeButton;
    private String currentUserId;
    private String currentUserRole;

    public OrdersView(Connection connection, String userId, String userRole) {
        this.currentUserId = userId;
        this.currentUserRole = userRole;

        // JFrame settings
        this.setTitle("Orders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1800, 600);

        // Initialize DatabaseOperations
        dbOps = new DatabaseOperations();

        // Initialize ordersTable
        ordersTable = new JTable();

        // Panel setup
        JPanel panel = new JPanel();
        this.add(panel, BorderLayout.NORTH);

        // Set Layout
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create buttons
        fulfilledOrdersButton = new JButton("View fulfilled orders");
        confirmedOrdersButton = new JButton("View confirmed orders");
        pendingOrdersButton = new JButton("View pending orders");
        deleteOrderLineButton = new JButton("Delete Selected Order Line");
        editButton = new JButton("Edit Selected Order");
        deleteButton = new JButton("Delete Selected Order");
        payButton = new JButton("Pay");
        homeButton = new JButton("Home");
        deleteOrderLineButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        payButton.setEnabled(false);

        // Add buttons to Panel
        panel.add(fulfilledOrdersButton);
        panel.add(confirmedOrdersButton);
        panel.add(pendingOrdersButton);
        panel.add(deleteButton);
        panel.add(editButton);
        panel.add(deleteOrderLineButton);
        panel.add(payButton);
        panel.add(homeButton);
        // Add table to Frame
        this.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        // Add action listeners to buttons
        fulfilledOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOrders("Fulfilled", connection);
            }
        });

        confirmedOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOrders("Confirmed", connection);
            }
        });

        pendingOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOrders("Pending", connection);
                deleteButton.setEnabled(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedOrders(connection);
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedOrder(connection);
            }
        });

        deleteOrderLineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedOrderLine(connection);
            }
        });

        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processPayment(connection);
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToMainScreen(connection, userId, userRole);
            }
        });
    }

    //methods

    private void displayOrders(String status, Connection connection) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        rs = dbOps.getOrdersByStatusAndUserId(status, this.currentUserId, connection);

        String[] columnNames = { "Order Number", "Customer ID", "Order Status", "Order Date", "Total Cost" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        while (rs != null && rs.next()) {
            int orderNumber = rs.getInt("order_number");
            String customerID = rs.getString("customer_ID");
            String orderStatus = rs.getString("order_status");
            Date orderDate = rs.getDate("order_date");
            BigDecimal totalCost = rs.getBigDecimal("totalCost");

            model.addRow(new Object[] { orderNumber, customerID, orderStatus, orderDate, totalCost });
        }

        ordersTable.setModel(model);

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    private void deleteSelectedOrders(Connection connection) {
        int[] selectedRows = ordersTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "No order selected!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected orders?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            for (int row : selectedRows) {
                int orderNumber = (int) ordersTable.getValueAt(row, 0);
                dbOps.deleteOrder(orderNumber, connection);
            }

            String currentStatus = "Pending";
            displayOrders(currentStatus, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while deleting orders.");
        }
    }

    private void editSelectedOrder(Connection connection) {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No order selected!");
            return;
        }

        int orderNumber = (int) ordersTable.getValueAt(selectedRow, 0);
        displayOrderLines(orderNumber, connection);
    }

    private void displayOrderLines(int orderNumber, Connection connection) {
        try {
            ResultSet rs = dbOps.getOrderLinesByOrderNumber(orderNumber, connection);
    
            String[] columnNames = {"Order Line Number", "Product Code", "Product Num", "Line Cost"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 2;
                }
            };
    
            while (rs.next()) {
                int orderLineNumber = rs.getInt("order_line_number");
                String productCode = rs.getString("product_code");
                int productNum = rs.getInt("product_num");
                BigDecimal lineCost = rs.getBigDecimal("line_cost");
    
                model.addRow(new Object[]{orderLineNumber, productCode, productNum, lineCost});
            }
    
            ordersTable.setModel(model);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        private void deleteSelectedOrderLine(Connection connection) {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No order line selected!");
                return;
            }
        
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected order line?");
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        
            try {
                int orderNumber = (int) ordersTable.getValueAt(selectedRow, 0);
                int orderLineNumber = (int) ordersTable.getValueAt(selectedRow, 1);
                dbOps.deleteOrderLine(orderNumber, orderLineNumber, connection);
        
                displayOrderLines(orderNumber, connection);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while deleting order line.");
            }
        
        
    }

    private void processPayment(Connection connection) {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No order selected!");
            return;
        }

        int orderNumber = (int) ordersTable.getValueAt(selectedRow, 0);
        try {
            String cardId = dbOps.userHasBankDetails(connection, currentUserId);
            if (cardId != null) {
                dbOps.updateOrderStatus(orderNumber, "Confirmed", connection);
                JOptionPane.showMessageDialog(this, "Payment successful!");
            } else {
                JOptionPane.showMessageDialog(this, "No bank details found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred during payment process.");
        }
    }

    private void goToMainScreen(Connection connection, String userId, String userRole) {
        this.dispose();

        try {
            MainScreenView mainScreen = new MainScreenView(connection, userId, userRole);
            mainScreen.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
