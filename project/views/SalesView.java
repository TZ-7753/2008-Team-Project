package project.views;

import project.model.DatabaseOperations;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

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
                DefaultTableModel model = new DefaultTableModel(new String[]{"Order Number", "Customer ID", "Order Status", "Order Date", "Total Cost"," "}, 0);
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getInt("order_number"), rs.getString("customer_ID"), rs.getString("order_status"), rs.getDate("order_date"), rs.getBigDecimal("totalCost")});
                }
                ordersTable.setModel(model);
                ordersTable.getColumn(" ").setCellRenderer(new OrdersView.ButtonRenderer());
                ordersTable.getColumn(" ").setCellEditor(new OrdersView.ButtonEditor(new JCheckBox(), connection));
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
        StaffViewWindow staffView = new StaffViewWindow(connection, userId, userRole);
        staffView.setVisible(true);
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
