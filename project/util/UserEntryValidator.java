package project.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.inventory.InventoryProductSearchResultsPanel;
import project.views.inventory.InventoryProductSearchWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class UserEntryValidator {
    public static boolean validStringLength(String text, int characterLimit) {
        boolean result;
        if (text.length() > characterLimit) {
            result = false;
        } else {result = true;}

        return result;
    }

    public static boolean validInt(String text) {
        boolean result = false;
        try {
            int entry = Integer.parseInt(text);
            result = true;
        } catch (NumberFormatException e) {}
        return result;
    }

    public static boolean validBigDecimal(String text) {
        boolean result = false;
        try {
            BigDecimal entry = new BigDecimal(text);
            result = true;
        } catch (Exception e) {}
        return result;
    }

    public static boolean validProductCode(String pC, String category) {
        boolean result;
        char start = pC.charAt(0);
        switch(category){
            case "Rolling Stock":
                result = (start == 'S');
                break;
            case "Locomotive":
                result = (start == 'L');
                break;
            case "Track Piece":
                result = (start == 'R');
                break;
            case "Controller":
                result = (start == 'C');
                break;
            case "Train Set":
                result = (start == 'M');
                break;
            default:
                result = (start == 'P');
                break;
        }

        return result;
    }

    public static boolean validDCC(String text) {
        boolean result = false;
        List<String> validSet = new ArrayList<String>();
        validSet.add("Analogue");
        validSet.add("DCC-Ready");
        validSet.add("DCC-Fitted");
        validSet.add("DCC-Sound");
        for (String s : validSet) {
            if (s.equals(text)) {
                result = true;
            }
        }
        return result;
    }

    public static boolean validTrainSetParts(List<String> productCodes, Connection connection) {
        DatabaseOperations databaseOperations = new DatabaseOperations();
        List<String> databaseProducts = new ArrayList<String>();
        try {
            String selectSQL = "SELECT product_code from train_set_parts";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                databaseProducts.add(resultSet.getString("product_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean result = true;
        for (String x : productCodes) {
            boolean found = false;
            for (String s : databaseProducts) {
                if (s.equals(x)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
            }
        }
        return result;
    }

    public static boolean validTrackPackParts(List<String> productCodes, Connection connection) {
        DatabaseOperations databaseOperations = new DatabaseOperations();
        List<String> databaseProducts = new ArrayList<String>();
        try {
            String selectSQL = "SELECT product_code from track_piece";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                databaseProducts.add(resultSet.getString("product_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean result = true;
        for (String x : productCodes) {
            boolean found = false;
            for (String s : databaseProducts) {
                if (s.equals(x)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
            }
        }
        return result;
    }

    public static List<String> validateNewProductWindow(List<String> productInfo, List<Integer> partQuantity, String category) {
        List<String> errorMessages = new ArrayList<String>();

        if (!(UserEntryValidator.validStringLength(productInfo.get(0), 6))) {
            errorMessages.add("Product Code must be 6 or less characters.");
        }
        if (!(UserEntryValidator.validStringLength(productInfo.get(1), 30))) {
            errorMessages.add("Product Name must be 30 or less characters.");
        }
        if (!(UserEntryValidator.validStringLength(productInfo.get(2), 30))) {
            errorMessages.add("Product Brand must be 30 or less characters.");
        }
        if (!(UserEntryValidator.validStringLength(productInfo.get(4), 30))) {
            errorMessages.add("UK Gauge must be 30 or less characters.");
        }
        for (String s : productInfo) {
            if (s.length() == 0) {
                errorMessages.add("Please fill all fields");
            }
        }
        if (!(UserEntryValidator.validBigDecimal(productInfo.get(3)))) {
            errorMessages.add("Invalid price value");
        }
        if (!(UserEntryValidator.validInt(productInfo.get(5)))) {
            errorMessages.add("Invalid stock");
        }
        switch (category) {
            case "Track Piece":
                if (!(UserEntryValidator.validProductCode(productInfo.get(0), category))) {
                    errorMessages.add("Product Codes for Track Pieces must start with R");
                }
                break;
            case "Rolling Stock":
                if (!(UserEntryValidator.validProductCode(productInfo.get(0), category))) {
                    errorMessages.add("Product Codes for Rolling Stock must start with S");
                }
                if(!(UserEntryValidator.validStringLength(productInfo.get(6), 30))) {
                    errorMessages.add("");
                }
                break;
        }

        return errorMessages;
    }


}
