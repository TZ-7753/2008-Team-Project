package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
public class InventoryProductSearchEntriesPanel extends JPanel {
    public InventoryProductSearchEntriesPanel(List<String> productCodes, List<String> productNames) {
        setLayout(new GridLayout(0,1));
        for (int i = 0; i < productCodes.size(); i++) {
            add(new JTextField("Code:" + productCodes.get(i) + "; Product Name:" + productNames.get(i)));
        }
    }
}
