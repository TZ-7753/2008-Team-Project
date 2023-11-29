package project.views.inventory;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.util.UserEntryValidator;
import project.views.StaffViewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class NewProductWindow extends JFrame implements ActionListener{
    private static final long serialVersionUID = 3L;
    private String itemSelected;
    private JComboBox<String> productOptions;
    private Container contentPane;
    private NewProductInfoPanel productInfoForm;

    private Connection connection;

    public NewProductWindow(Connection con, String userID, String userRole) throws HeadlessException {
        setTitle("Create New Product");

        connection = con;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        productOptions = new JComboBox<String>();
        productOptions.addItem("Track Piece");
        productOptions.addItem("Rolling Stock");
        productOptions.addItem("Controller");
        productOptions.addItem("Track Pack");
        productOptions.addItem("Train Set");
        productOptions.addItem("Locomotive");
        productOptions.setSelectedItem("Track Piece");
        productOptions.addActionListener(this);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(productOptions , BorderLayout.NORTH);

        itemSelected = productOptions.getSelectedItem().toString();
        productInfoForm = new NewProductInfoPanel(this, itemSelected);
        contentPane.add(productInfoForm, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        contentPane.add(submitButton, BorderLayout.SOUTH);

        JMenu navigationBar = new JMenu("Nav");

        JMenuItem dv = new JMenuItem("Default View");
        navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

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
                }
            });
        }
        navigationBar.addSeparator();
        JMenuItem iv = new JMenuItem("Back");
        navigationBar.add(iv);

        iv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InventoryWindow iw = new InventoryWindow(connection, userID, userRole);
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
        if (command.equals("Submit")) {
            List<JTextField> productFields = productInfoForm.getProductInfoEntry().getProductTextFields();
            int partCount = productInfoForm.getProductInfoEntry().getPartCount();
            char productType;
            switch (itemSelected) {
                case "Locomotive":
                    productType = 'L';
                    break;
                case "Track Piece":
                    productType = 'R';
                    break;
                case "Rolling Stock":
                    productType = 'S';
                    break;
                case "Controller":
                    productType = 'C';
                    break;
                case "Track Pack":
                    productType = 'P';
                    break;
                default:
                    productType = 'M';
                    break;
            }
            List<String> productInfo = new ArrayList<String>();
            List<Integer> partQuantity = new ArrayList<Integer>();

            switch (productType) {
                case 'C':
                    for (int i = 0; i < 6; i++) {
                        productInfo.add((productFields.get(i)).getText());
                    }
                    productInfo.add(String.valueOf((productInfoForm.getProductInfoEntry().getControllerDigital()).getSelectedItem()));
                default:
                    for (JTextField p : productFields) {
                        productInfo.add(p.getText());
                    }
            }

            if ((productType == 'M') || (productType == 'P')) {
                List<JComboBox<Integer>> partQuantities = productInfoForm.getProductInfoEntry().getPartQuantityBoxes();
                for (JComboBox<Integer> p : partQuantities) {
                    partQuantity.add(Integer.valueOf(String.valueOf(p.getSelectedItem())));
                }
            }



            DatabaseOperations databaseOperations = new DatabaseOperations();
            if (productInfo.get(0).charAt(0) != productType) {
                JOptionPane.showMessageDialog(this, "Product Code does not match category.");
            } else {
                try {
                    databaseOperations.insertProduct(connection, productType, productInfo, partQuantity);
                    JOptionPane.showMessageDialog(this, "Product Creation Successful");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid Input, please correct errors.");
                    try {
                        databaseOperations.deleteProduct(productInfo.get(0), connection);
                    } catch (Exception vee) {
                        vee.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        } else {
            if (!(itemSelected.equals(productOptions.getSelectedItem().toString()))) {
                contentPane.remove(productInfoForm);
                itemSelected = productOptions.getSelectedItem().toString();
                productInfoForm = new NewProductInfoPanel(this, itemSelected);
                contentPane.add(productInfoForm, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        }

    }

    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();
            NewProductWindow npr = new NewProductWindow( databaseConnectionHandler.getConnection(), "werwer", "Manager");

        }catch (Throwable t) {
            databaseConnectionHandler.closeConnection();
            throw new RuntimeException(t);
        }
    }
}
