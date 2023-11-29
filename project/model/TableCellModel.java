package project.model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableCellModel extends DefaultTableModel {

    public TableCellModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    // Make only the button column (the last column) editable
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == getColumnCount() - 1;
    }

    // Define the button column (the last column) as JButton types
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == getColumnCount() - 1) {
            return JButton.class;
        }
        return String.class;
    }
}
