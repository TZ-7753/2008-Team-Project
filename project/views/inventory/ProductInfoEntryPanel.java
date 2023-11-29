package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
public class ProductInfoEntryPanel extends JPanel{

    private List<JTextField> productTextFields = new ArrayList<JTextField>();
    private JComboBox<String> controllerDigital = new JComboBox<String>();
    private List<JComboBox<Integer>> partQuantityBoxes = new ArrayList<JComboBox<Integer>>();
    private int partCount;

    public int getPartCount() {
        return this.partCount;
    }
    public List<JTextField> getProductTextFields() {
        return this.productTextFields;
    }

    public JComboBox<String> getControllerDigital() {
        return this.controllerDigital;
    }

    public List<JComboBox<Integer>> getPartQuantityBoxes() {
        return this.partQuantityBoxes;
    }
    public ProductInfoEntryPanel(String option, Boolean boxedSet, int itemCount) {
        setLayout(new GridLayout(0,1));

        JTextField productCodeField = new JTextField();
        JTextField productNameField = new JTextField();
        JTextField productBrandField = new JTextField();
        JTextField productRetailPriceField = new JTextField();
        JTextField productUKGaugeField = new JTextField();
        JTextField productStockField = new JTextField();

        add(productCodeField);
        add(productNameField);
        add(productBrandField);
        add(productRetailPriceField);
        add(productUKGaugeField);
        add(productStockField);

        productTextFields.add(productCodeField);
        productTextFields.add(productNameField);
        productTextFields.add(productBrandField);
        productTextFields.add(productRetailPriceField);
        productTextFields.add(productUKGaugeField);
        productTextFields.add(productStockField);

        JTextField eraCodeField = new JTextField();

        switch (option) {
            case "Rolling Stock":
                add(eraCodeField);
                productTextFields.add(eraCodeField);
                break;
            case "Locomotive":
                JTextField dccCodeField = new JTextField();
                add(eraCodeField);
                add(dccCodeField);
                productTextFields.add(eraCodeField);
                productTextFields.add(dccCodeField);
                break;
            case "Controller":
                controllerDigital = new JComboBox<String>();
                controllerDigital.addItem("yes");
                controllerDigital.addItem("no");
                add(controllerDigital);
            default:
                break;
        }

        if (boxedSet) {
            JTextField [] setPartList = new JTextField[itemCount];
            List<JComboBox<Integer>> setPartQuantityList = new ArrayList<JComboBox<Integer>>();
            JTextField currentPart = new JTextField();
            JComboBox<Integer> currentPartQuantity = new JComboBox<Integer>();
            for (int i = 1; i <= itemCount; i++) {
                currentPart = new JTextField();
                currentPartQuantity = new JComboBox<Integer>();
                for (int x = 1; x <= 100; x ++) {
                    currentPartQuantity.addItem(x);
                }
                currentPartQuantity.setSelectedIndex(0);
                setPartList[i-1] = currentPart;
                setPartQuantityList.add(currentPartQuantity);
            }
            for (int i = 0; i < itemCount; i++) {
                add(setPartList[i]);
                add(setPartQuantityList.get(i));
            }
            for (JTextField j : setPartList) {
                productTextFields.add(j);
            }
            for (JComboBox<Integer> j : setPartQuantityList) {
                partQuantityBoxes.add(j);
            }
            partCount = setPartQuantityList.size();
        }

    }
}
