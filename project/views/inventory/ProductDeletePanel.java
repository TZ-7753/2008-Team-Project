package project.views.inventory;
import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
public class ProductDeletePanel extends JPanel implements ActionListener{
    private Connection connection;
    private JButton delete;
    private JTextField deleteProduct;
    public ProductDeletePanel(DeleteProductWindow delprodwin, Connection con) {
        connection = con;

        setLayout(new GridLayout(1,0));
        deleteProduct = new JTextField();
        add(deleteProduct);

        delete = new JButton("Delete");
        add(delete);
        delete.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Delete")) {
            DatabaseOperations databaseOperations = new DatabaseOperations();
            String deleteCode = deleteProduct.getText();
            try {
                if (databaseOperations.productExists(deleteCode, connection)) {
                    JOptionPane.showMessageDialog(this, "Succesfully Removed from Inventory");
                } else {
                    JOptionPane.showMessageDialog(this, "Does not Exist in Inventory");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                databaseOperations.deleteProduct(deleteCode, connection);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "There was an error in attempting to delete your product.");
            }

        }
    }
}
