package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AssociatedBoxesPanel extends JPanel{
    public AssociatedBoxesPanel(List<String> info) {
        setLayout(new GridLayout(0,5));
        for (String s : info) {
            add(new JTextField(s));
        }

    }
}
