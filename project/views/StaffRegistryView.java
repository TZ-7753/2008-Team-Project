package project.views;

import project.model.DatabaseOperations;
import project.model.TableCellModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class StaffRegistryView extends JFrame {

    //Build data for inheriting
    private Connection connection;
    private String userID;
    private String userRole;

    public StaffRegistryView(Connection connection, String userID, String userRole) throws SQLException {

        // Create the JFrame in the constructor
        setTitle("Staff Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);

        /**
         * Creating Navigation menu. ---------------------------------------------------------------------------------
         */
        JMenu navigationBar = new JMenu("Menu");

        JMenuItem dv = new JMenuItem("Main Screen");
        navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    dispose();
                    MainScreenView mainScreenView = new MainScreenView(connection,userID,userRole);
                    mainScreenView.setVisible(true);
                }catch (SQLException err){
                    err.printStackTrace();
                }
            }
        });

        if (userRole.equals("Manager") || userRole.equals("Staff")) {
            JMenuItem sv = new JMenuItem("Staff View");
            navigationBar.addSeparator();
            navigationBar.add(sv);
            sv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    StaffViewWindow sfw = new StaffViewWindow(connection, userID, userRole);
                    sfw.setVisible(true);
                }
            });

        }

        if (userRole.equals("Manager")) {
            JMenuItem sr = new JMenuItem("Staff Registry");
            navigationBar.addSeparator();
            navigationBar.add(sr);
            sr.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try{
                        dispose();
                        StaffRegistryView staffRegistryView = new StaffRegistryView(connection,userID,userRole);
                        staffRegistryView.setVisible(true);
                    }catch (SQLException err){
                        err.printStackTrace();
                    }
                }
            });
        }
        /**
         * --------------------------------------------------------------------------------------------------------
         */

        // Define column names for Jtable
        String[] columns = {"ID", "First Name", "Last Name","Role"," "};

        // Fetch customer data from DB
        ArrayList<String[]> users = DatabaseOperations.filterUsers(connection);
        Object[][] tableData = new Object[users.size()][columns.length];

        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < users.get(i).length; j++) {
                tableData[i][j] = users.get(i)[j];
            }
            tableData[i][columns.length - 1] = "Register"; // Button text
        }

        // Create Jtable
        TableCellModel model = new TableCellModel(tableData, columns);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        /*
        // Set up the TableRowSorter on the table
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        // Specify the columns of first name & Role to be sortable
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING)); // First Name column
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING)); // Role column
        sorter.setSortKeys(sortKeys);
        sorter.setSortable(0, false);   //Make the column of ID unsortable
        */

        // Render registration buttons
        table.getColumn(" ").setCellRenderer(new ButtonRenderer());
        table.getColumn(" ").setCellEditor(new ButtonEditor(new JCheckBox(), connection,this));

        // Create a JPanel for other nav buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create and add buttons to the panel
        JButton staffViewButton = new JButton("Staff View");
        JButton mainScreenButton = new JButton("Main Screen");

        buttonsPanel.add(staffViewButton);
        buttonsPanel.add(mainScreenButton);

        // Add the buttons panel below the table
        add(buttonsPanel, BorderLayout.SOUTH);


        staffViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                StaffViewWindow staffViewWindow = new StaffViewWindow(connection, userID, userRole);
            }
        });

        mainScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    StaffRegistryView.this.dispose();
                    MainScreenView mainScreen = new MainScreenView(connection, userID, userRole);
                    mainScreen.setVisible(true);
                }catch (SQLException error){
                    error.printStackTrace();
                }
            }
        });

        //Pass inheriting data
        this.connection = connection;
        this.userID = userID;
        this.userRole = userRole;
    }

     //Refresh method. Re-collects altered data from DB, and reload the page.
    public void reloadWindow() {
        this.dispose();
        EventQueue.invokeLater(() -> {
            try {
                StaffRegistryView newWindow = new StaffRegistryView(connection, userID, userRole);
                newWindow.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
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
        public ButtonEditor(JCheckBox checkBox, Connection connection, StaffRegistryView parent) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getEditingRow();
                    if(Objects.equals(table.getModel().getValueAt(selectedRow, 3).toString(), "Customer")){
                        String name = table.getModel().getValueAt(selectedRow, 1).toString() + " "+ table.getModel().getValueAt(selectedRow, 2).toString();
                        String role = "Staff";
                        DatabaseOperations databaseOperations = new DatabaseOperations();
                        databaseOperations.roleRegister(connection, table.getModel().getValueAt(selectedRow, 0).toString(), role);
                        JOptionPane.showMessageDialog(button, name + " is now promoted to Staff.");
                    }else if(Objects.equals(table.getModel().getValueAt(selectedRow, 3).toString(), "Staff")){
                        String name = table.getModel().getValueAt(selectedRow, 1).toString() + " "+ table.getModel().getValueAt(selectedRow, 2).toString();
                        String role = "Customer";
                        DatabaseOperations databaseOperations = new DatabaseOperations();
                        databaseOperations.roleRegister(connection, table.getModel().getValueAt(selectedRow, 0).toString(), role);
                        JOptionPane.showMessageDialog(button, name + " is now demoted to Customer.");
                    }
                    parent.reloadWindow();
                    isPushed = true;
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            int selectedRow = table.getEditingRow();
            String role = (String) table.getModel().getValueAt(row, 3);

            if (role.equals("Customer")) {
                button.setText("Promote");
            } else {
                button.setText("Demote");
            }
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
            String role = (String) table.getModel().getValueAt(row, 3);

            if (role.equals("Customer")) {
                this.setText("Promote");
            } else {
                this.setText("Demote");
            }
            return this;
        }
    }
}
