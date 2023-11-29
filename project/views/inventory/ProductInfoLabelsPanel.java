package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
public class ProductInfoLabelsPanel extends JPanel{

    public ProductInfoLabelsPanel(String option, Boolean boxedSet, int itemCount) {
        setLayout(new GridLayout(0,1));
        add(new JLabel("product code"));
        add(new JLabel("product name"));
        add(new JLabel("brand"));
        add(new JLabel("retail price"));
        add(new JLabel("UK gauge"));
        add(new JLabel("stock"));

        switch (option) {
            case "Rolling Stock":
                add(new JLabel("era code"));
                break;
            case "Locomotive":
                add(new JLabel("era code"));
                add(new JLabel("DCC Code"));
                break;
            case "Controller":
                add(new JLabel("Is Digital?"));
            default:
                break;
        }

        if (boxedSet) {
            for (int i = 1; i <= itemCount; i++) {
                add(new JLabel("part product code"));
                add(new JLabel("part quantity"));
            }
        }

    }
}
