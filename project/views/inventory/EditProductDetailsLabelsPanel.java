package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
public class EditProductDetailsLabelsPanel extends JPanel{
    public EditProductDetailsLabelsPanel(String productCode, int partCount){
        setLayout(new GridLayout(0,1));
        char productType = productCode.charAt(0);

        add(new JLabel("product name"));
        add(new JLabel("brand"));
        add(new JLabel("retail price"));
        add(new JLabel("UK gauge"));
        add(new JLabel("stock"));

        switch (productType) {
            case 'S':
                add(new JLabel("era code"));
                break;
            case 'L':
                add(new JLabel("era code"));
                add(new JLabel("DCC Code"));
                break;
            case 'C':
                add(new JLabel("Is Digital?"));
            default:
                break;
        }

        if ((productType == 'M') || (productType == 'P')) {
            for (int i = 0; i < partCount; i++) {
                add(new JLabel("product code"));
                add(new JLabel("part quantity"));
            }
        }
    }
}
