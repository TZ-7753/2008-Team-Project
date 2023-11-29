package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewProductInfoPanel extends JPanel implements ActionListener{
    private NewProductWindow newproductwindow;
    private int itemSelected = 1;
    private JComboBox<Integer> itemNumber;
    private ProductInfoLabelsPanel productInfoLabels;
    private ProductInfoEntryPanel productInfoEntry;
    private String productType;
    private Boolean boxedSet;

    public ProductInfoEntryPanel getProductInfoEntry() {
        return productInfoEntry;
    }

    public NewProductInfoPanel(NewProductWindow newprodwin, String option) {
        newproductwindow = newprodwin;
        productType = option;
        boxedSet = false;
        setLayout(new BorderLayout());
        if ((option.equals("Track Pack")) || option.equals("Train Set")) {
            boxedSet = true;
            itemNumber = new JComboBox<Integer>();
            for (int i = 1; i < 100; i++) {
                itemNumber.addItem(i);
            }
            itemNumber.setSelectedIndex(0);
            itemNumber.addActionListener(this);
            add(itemNumber, BorderLayout.NORTH);
        }
        productInfoEntry = new ProductInfoEntryPanel(productType, boxedSet, itemSelected);
        productInfoLabels = new ProductInfoLabelsPanel(productType, boxedSet, itemSelected);
        add(productInfoLabels, BorderLayout.WEST);
        add(productInfoEntry, BorderLayout.CENTER);

    }

    public void actionPerformed(ActionEvent event) {
        if (itemSelected != (int) itemNumber.getSelectedItem()) {
            itemSelected = (int) itemNumber.getSelectedItem();
            remove(productInfoEntry);
            remove(productInfoLabels);
            productInfoEntry = new ProductInfoEntryPanel(productType, boxedSet, itemSelected);
            productInfoLabels = new ProductInfoLabelsPanel(productType, boxedSet, itemSelected);
            add(productInfoLabels, BorderLayout.WEST);
            add(productInfoEntry, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }
}