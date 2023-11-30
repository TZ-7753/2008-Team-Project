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
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import com.mysql.cj.protocol.a.SqlDateValueEncoder;
import java.util.Date;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

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
    private Container contentPane = getContentPane();

    public OrdersView(Connection connection, String userId, String userRole) {
        this.currentUserId = userId;
        this.currentUserRole = userRole;

        // JFrame settings
        this.setTitle("Orders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 600);

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
                processPayment(connection, userId, userRole);
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToMainScreen(connection, userId, userRole);
            }
        });
    }

    // methods

    public void displayOrders(String status, Connection connection) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            rs = dbOps.getOrdersByStatusAndUserId(status, this.currentUserId, connection);

        String[] columnNames = { "Order Number", "Customer ID", "Order Status", "Order Date", "Total Cost"," "};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        ordersTable.setModel(model);
        while (rs != null && rs.next()) {
            int orderNumber = rs.getInt("order_number");
            String customerID = rs.getString("customer_ID");
            String orderStatus = rs.getString("order_status");
            Date orderDate = rs.getDate("order_date");
            BigDecimal totalCost = rs.getBigDecimal("totalCost");

            model.addRow(new Object[] { orderNumber, customerID, orderStatus, orderDate, totalCost });
        }
        ordersTable.getColumn(" ").setCellRenderer(new OrdersView.ButtonRenderer());
        ordersTable.getColumn(" ").setCellEditor(new OrdersView.ButtonEditor(new JCheckBox(), connection));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        boolean isPending = "Pending".equals(status);
        deleteButton.setEnabled(isPending);
        editButton.setEnabled(isPending);
        deleteOrderLineButton.setEnabled(isPending);
        payButton.setEnabled(isPending);
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
        OrderLineEditView orderLineEditView = new OrderLineEditView(connection, orderNumber, this.dbOps, this);
        orderLineEditView.setVisible(true);

    }

    private void displayOrderLines(int orderNumber, Connection connection) {
        try {
            ResultSet rs = dbOps.getOrderLinesByOrderNumber(orderNumber, connection);

            String[] columnNames = { "Order Line Number", "Product Code", "Product Num", "Line Cost" };
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

                model.addRow(new Object[] { orderLineNumber, productCode, productNum, lineCost });
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

    private void processPayment(Connection connection, String userID, String userRole) {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No order selected!");
            return;
        }

        int orderNumber = (int) ordersTable.getValueAt(selectedRow, 0);

        String cardId = dbOps.userHasBankDetails(connection, currentUserId);
        if (cardId == null) {
            try {
                AddBankDetailsView addBankDetailsView = new AddBankDetailsView(connection, userID, userRole);
                addBankDetailsView.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cardId = dbOps.userHasBankDetails(connection, currentUserId);
        } else {
            try {
                if (cardId != null) {
                    dbOps.updateOrderStatus(orderNumber, "Confirmed", connection);
                    JOptionPane.showMessageDialog(this, "Payment Success");
                    dispose();
                    OrdersView ordersView = new OrdersView(connection, userID, userRole);
                    ordersView.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No bank details found!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred during payment process.");
            }
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

    /***
     * ButtonEditor and ButtonRenderer are modifications of DefaultCellEditor.
     * ButtonEditor is in type of JCheckBox, functions and listens as a JButton here,
     *          upon clicked (regardless of ticked or not), it registers the user in its row as Staff.
     * ButtonRenderer does essentially nothing but to render a 'fake' and opaque JButton above the checkbox.
     * This is because JButton is not accepted as a type of cell in the JTable.
     * In some ways, JCheckBoxes can do the same thing as JButtons.
     */
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private JTable table;
        private boolean isPushed;
        public ButtonEditor(JCheckBox checkBox, Connection connection) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getEditingRow();
                    int orderNum = (int)table.getModel().getValueAt(selectedRow, 0);
                    try {
                        DatabaseOperations databaseOperations = new DatabaseOperations();
                        List<String> orderDetails = databaseOperations.retrieveOrderDetails(connection,orderNum);
                        StringBuilder orderLines = new StringBuilder();
                        if(orderDetails.size() > 11){
                            for(int i = 11; i < orderDetails.size(); i += 4){
                                orderLines.append("OrderLine Number: ").append(orderDetails.get(i)).append("\n");
                                orderLines.append("OrderLine Product code: ").append(orderDetails.get(i+1)).append("\n");
                                orderLines.append("OrderLine Product amount: ").append(orderDetails.get(i+2)).append("\n");
                                orderLines.append("OrderLine Cost: ").append(orderDetails.get(i+3)).append("\n");
                                orderLines.append("\n");
                            }
                        }
                        JOptionPane.showMessageDialog(button,
                                "Order ID: " + orderDetails.get(0) + "\n"
                                        + "Customer ID: " + orderDetails.get(1) + "\n"
                                        + "Order date: " + orderDetails.get(2) + "\n"
                                        + "Total cost: " + orderDetails.get(3) + "\n"
                                        + "\n\n"
                                        + "Customer Name: " + orderDetails.get(4) + " "+ orderDetails.get(5) + "\n"
                                        + "Email address: " + orderDetails.get(6) + "\n"
                                        + "House Number: " + orderDetails.get(7) + "\n"
                                        + "Postcode: " + orderDetails.get(8) + "\n"
                                        + "Road Name: " + orderDetails.get(9) + "\n"
                                        + "City Name: " + orderDetails.get(10) + "\n"
                                        + "\n\n" + orderLines.toString());
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }
                    isPushed = true;
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            int selectedRow = table.getEditingRow();
            int orderNum = (int)table.getModel().getValueAt(row, 0);
            button.setText("Details");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {isPushed = false;}
            return super.getCellEditorValue();
        }
    }

    //Render a button above the checkbox, does not do anything on its own, only providing the shape
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int selectedRow = table.getEditingRow();
            this.setText("Details");
            return this;
        }
    }
}
