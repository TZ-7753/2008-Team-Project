package project.views.inventory;

import project.model.DatabaseOperations;
import project.views.MainScreenView;
import project.views.StaffRegistryView;
import project.views.StaffViewWindow;
import project.views.inventory.EditProductDetailsEntriesPanel;
import project.views.inventory.EditProductDetailsLabelsPanel;

import project.model.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class EditProductDetailsWindow extends JFrame implements ActionListener{
    private Connection connection;
    private int partCount = 0;
    private EditProductDetailsEntriesPanel editProductDetailsEntriesPanel;
    private EditProductDetailsLabelsPanel editProductDetailsLabelsPanel;
    private JButton update;
    private String productCode;
    private JComboBox<Integer> newPartCountBox;
    private int newPartCount = 0;
    private Container contentPane;

    public EditProductDetailsWindow(String pC, Connection con, String userID, String userRole) throws HeadlessException{

        connection = con;
        productCode = pC;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());


        if ((productCode.charAt(0) == 'M') || (productCode.charAt(0) == 'P')){
            DatabaseOperations databaseOperations = new DatabaseOperations();

            try {
                partCount = databaseOperations.partCount(productCode, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            newPartCountBox = new JComboBox<Integer>();
            for (int x = 1; x <= 100; x++) {
                newPartCountBox.addItem(x);
            }
            newPartCountBox.setSelectedItem(partCount);
            newPartCountBox.addActionListener(this);
            newPartCount = partCount;
            contentPane.add(newPartCountBox, BorderLayout.NORTH);
        }

        editProductDetailsEntriesPanel = new EditProductDetailsEntriesPanel(productCode, partCount, newPartCount, connection);
        editProductDetailsLabelsPanel = new EditProductDetailsLabelsPanel(productCode, partCount);

        contentPane.add(editProductDetailsEntriesPanel, BorderLayout.CENTER);
        contentPane.add(editProductDetailsLabelsPanel, BorderLayout.WEST);

        update = new JButton("update");
        contentPane.add(update, BorderLayout.SOUTH);
        update.addActionListener(this);



        JMenu navigationBar = new JMenu("Menu");

        JMenuItem dv = new JMenuItem("Main Screen");
        navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    MainScreenView msv = new MainScreenView(connection, userID, userRole);
                    msv.setVisible(true);
                } catch (SQLException err) {
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
                    try {
                        StaffRegistryView srv = new StaffRegistryView(connection, userID, userRole);
                        srv.setVisible(true);
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }
                }
            });
        }


        JMenuItem back = new JMenuItem("Back");
        navigationBar.addSeparator();
        navigationBar.add(back);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InventoryProductSearchWindow ipsw = new InventoryProductSearchWindow(connection, userID, userRole);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("update")) {
            List<JTextField> textEntries = editProductDetailsEntriesPanel.getProductTextFields();
            List<JTextField> productPartTextFields = editProductDetailsEntriesPanel.getProductPartTextFields();
            JComboBox<String> controllerDigital = editProductDetailsEntriesPanel.getControllerDigital();
            List<JComboBox<Integer>> partQuantites = editProductDetailsEntriesPanel.getPartQuantityBoxes();

            List<String> newProductDetails = new ArrayList<String>();
            newProductDetails.add(productCode);
            for (JTextField j : textEntries) {
                newProductDetails.add(j.getText());
            }
            if (productCode.charAt(0) == 'C') {
                newProductDetails.add(String.valueOf(controllerDigital.getSelectedItem()));
            }
            for (int i = 0; i < newPartCount; i++) {
                newProductDetails.add(productPartTextFields.get(i).getText());
                newProductDetails.add(String.valueOf(partQuantites.get(i).getSelectedItem()));
            }
            DatabaseOperations databaseOperations = new DatabaseOperations();
            try {
                databaseOperations.updateProductDetails(newProductDetails, productCode, connection);
                JOptionPane.showMessageDialog(this, "Successfully Updated Details");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    databaseOperations.deleteProduct(productCode, connection);
                } catch (Exception vee) {
                    vee.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Invalid input, Fix errors");
            }

        } else {
            if ((int)(newPartCountBox.getSelectedItem()) != newPartCount) {
                newPartCount = (int) newPartCountBox.getSelectedItem();
                contentPane.remove(editProductDetailsEntriesPanel);
                contentPane.remove(editProductDetailsLabelsPanel);
                editProductDetailsEntriesPanel = new EditProductDetailsEntriesPanel(productCode, partCount, newPartCount,  connection);
                editProductDetailsLabelsPanel = new EditProductDetailsLabelsPanel(productCode, newPartCount);
                contentPane.add(editProductDetailsLabelsPanel, BorderLayout.WEST);
                contentPane.add(editProductDetailsEntriesPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        }
    }

    public static void main (String [] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();
            EditProductDetailsWindow npr = new EditProductDetailsWindow("CERT", databaseConnectionHandler.getConnection(), "werwer", "Manager");
            npr.setTitle("Hello");

        }catch (Throwable t) {
            databaseConnectionHandler.closeConnection();
            throw new RuntimeException(t);
        }
    }
}
