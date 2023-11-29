package project.views.customerproductsearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.inventory.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
public class CustomerProductSearchLabelsPanel extends JPanel {
    public CustomerProductSearchLabelsPanel(List<String> productCodes, Connection connection, CustomerProductSearchWindow cpsw, String category) {
        setLayout(new GridLayout(0,1));
        List<JButton> editButtons = new ArrayList<JButton>();
        for (int i = 1; i<= productCodes.size(); i++) {
            editButtons.add(new JButton("View/Add To Order"));
        }
        int[] indices = new int[productCodes.size()];
        int count = 0;
        for (JButton e : editButtons) {
            add(e);
            int choice = count;
            e.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    cpsw.dispose();
                    CustomerProductDetailsWindow npr = new CustomerProductDetailsWindow(productCodes.get(choice), connection, category, cpsw.getUserID(), cpsw.getUserRole());
                    npr.setTitle("Hello");
                }
            });
            count +=1;
        }
    }
}
