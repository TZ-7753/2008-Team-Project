package project.views.inventory;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
public class InventoryProductSearchLabelsPanel extends JPanel{
    public InventoryProductSearchLabelsPanel(List<String> productCodes, Connection connection, InventoryProductSearchWindow ipsw) {
        setLayout(new GridLayout(0,1));
        List<JButton> editButtons = new ArrayList<JButton>();
        for (int i = 1; i<= productCodes.size(); i++) {
            editButtons.add(new JButton("View/Edit"));
        }
        int[] indices = new int[productCodes.size()];
        int count = 0;
        for (JButton e : editButtons) {
            add(e);
            int choice = count;
            e.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    ipsw.dispose();
                    EditProductDetailsWindow npr = new EditProductDetailsWindow(productCodes.get(choice), connection, ipsw.getUserID(), ipsw.getUserRole());
                    npr.setTitle("Hello");
                }
            });
            count +=1;
        }
    }
}
