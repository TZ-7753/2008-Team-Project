package project.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.awt.BorderLayout;


public class OrderLineEditView extends JFrame {
    private JTable orderLinesTable;
    private JButton saveButton;
    private JButton deleteButton;
    private int orderNumber;
    private Connection connection;

    public OrderLineEditView(Connection connection, int orderNumber) {
        this.connection = connection;
        this.orderNumber = orderNumber;

        initialiseComponents();
        loadOrderLines();
    }

    private void initialiseComponents() {
        setTitle("Edit Order Lines");
        setSize(600, 400);
        setLayout(new BorderLayout());

        orderLinesTable = new JTable();
        add(new JScrollPane(orderLinesTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save Changes");
        deleteButton = new JButton("Delete Selected Line");

        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveChanges());
        deleteButton.addActionListener(e -> deleteSelectedOrderLine());
    }

    private void loadOrderLines() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Line Number", "Product Code", "Quantity", "Line Cost"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // 仅允许编辑数量列
            }
        };

        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM order_line WHERE order_number = ?");
            pstmt.setInt(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("order_line_number"),
                    rs.getString("product_code"),
                    rs.getInt("product_num"),
                    rs.getBigDecimal("line_cost")
                });
            }

            orderLinesTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order lines.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveChanges() {
        DefaultTableModel model = (DefaultTableModel) orderLinesTable.getModel();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            int orderLineNumber = (Integer) model.getValueAt(i, 0);
            int newQuantity = (Integer) model.getValueAt(i, 2);

            try {
                String sql = "UPDATE order_line SET product_num = ? WHERE order_number = ? AND order_line_number = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, orderNumber);
                pstmt.setInt(3, orderLineNumber);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating order line.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        loadOrderLines();
    }

    private void deleteSelectedOrderLine() {
        int selectedRow = orderLinesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No order line selected!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderLineNumber = (Integer) orderLinesTable.getValueAt(selectedRow, 0);

        try {
            String sql = "DELETE FROM order_line WHERE order_number = ? AND order_line_number = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setInt(1, orderNumber);
            pstmt.setInt(2, orderLineNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting order line.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        loadOrderLines();
    }
}
