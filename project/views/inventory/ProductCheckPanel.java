package project.views.inventory;

import project.model.DatabaseOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
public class ProductCheckPanel extends JPanel implements ActionListener{
    private Connection connection;
    private JTextArea userNotice;
    private JTextField checkProduct;
    private JButton submit;

    private AssociatedBoxesPanel associatedBoxesPanel;

    public ProductCheckPanel(DeleteProductWindow delprodwin, Connection con) {
        connection = con;

        setLayout(new GridLayout(0,1));

        userNotice = new JTextArea("Please Note: Deleting a product that is part of a boxed set, will remove it from the boxed set. " + System.lineSeparator()+ "To see affected sets, enter your product code in this field. The field at the bottom will delete the item.");
        userNotice.setEditable(false);
        add(userNotice);

        checkProduct = new JTextField();
        add(checkProduct);

        submit = new JButton("Submit");
        add(submit);
        submit.addActionListener(this);

        associatedBoxesPanel = new AssociatedBoxesPanel(new ArrayList<String>());

    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Submit")) {
            String itemSelected = checkProduct.getText();
            List<String> associatedBoxesResults = new ArrayList<String>();
            DatabaseOperations databaseOperations = new DatabaseOperations();

            try {
                associatedBoxesResults = databaseOperations.associatedSets(itemSelected, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            remove(associatedBoxesPanel);
            associatedBoxesPanel = new AssociatedBoxesPanel(associatedBoxesResults);
            add(associatedBoxesPanel);

            revalidate();
            repaint();
        }
    }
}
