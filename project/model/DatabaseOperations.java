package project.model;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import project.util.UniqueUserIDGenerator;

public class DatabaseOperations {

    public ArrayList<String> verifyLogin(Connection connection, String email, String hashedPassword) {
        ArrayList<String> returnMessage = new ArrayList<String>();
        try {
            String sqlQuery = "SELECT u.user_ID,u.password_hash,u.user_role FROM users u WHERE u.email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPasswordDB = resultSet.getString("password_hash");
                if (hashedPasswordDB.equals(hashedPassword)) {
                    returnMessage.add("success");
                    returnMessage.add(resultSet.getString("user_ID"));
                    returnMessage.add(resultSet.getString("user_role"));
                } else {
                    returnMessage.add("error");
                    returnMessage.add("Password is Incorrect!");
                } 
            } else {
                returnMessage.add("error");
                returnMessage.add("User with Email: " + email + " does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public String verifyAccountCreation(Connection connection, String firstName, String surname, String email, String hashedPassword, String houseNumber, String streetName, String cityName, String postcode){
        String returnMessage = "";
        try{
            String sqlQuery = "SELECT * FROM users u WHERE u.email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                sqlQuery = "SELECT * FROM user_address u WHERE u.house_number=? AND u.postcode=?";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, houseNumber);
                preparedStatement.setString(2, postcode);
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()) {
                    String insertSQL = "INSERT INTO user_address VALUES (?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, houseNumber);
                    preparedStatement.setString(2, streetName);
                    preparedStatement.setString(3, cityName);
                    preparedStatement.setString(4, postcode);
                    int rowsEffected = preparedStatement.executeUpdate();
                    System.out.println("address rows effected = " + rowsEffected);
                }
                String userID = UniqueUserIDGenerator.generateUniqueUserID();
                String insertSQL = "INSERT INTO users VALUES (?,?,?,'Customer',?,?,?,?)";
                preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, surname);
                preparedStatement.setString(4, houseNumber);
                preparedStatement.setString(5, postcode);
                preparedStatement.setString(6, email);
                preparedStatement.setString(7, hashedPassword);
                int rowsEffected = preparedStatement.executeUpdate();
                returnMessage = "User added: Rows effected - " + rowsEffected;
            } else {
                returnMessage = "User already exist with email: " + email + "!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public String [] getUserInfo(Connection connection, String userID){
        try{
            String sqlQuery = "SELECT * FROM users u, user_address a WHERE u.user_ID=? AND u.house_number = a.house_number AND u.postcode = a.postcode";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String [] userInfo = {resultSet.getString("first_name"), resultSet.getString("last_name"),
                                        resultSet.getString("email"), resultSet.getString("house_number"),
                                        resultSet.getString("road_name"), resultSet.getString("city_name"),
                                        resultSet.getString("postcode")};
                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updatePassword(Connection connection, String userID, String hashedPassword){
        try{
            String sqlUpdate = "UPDATE users u SET u.password_hash = ? WHERE u.user_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, userID);
            int rowsEffected = preparedStatement.executeUpdate();
            return "Rows effected - " + rowsEffected;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String [] updateUserInfo(Connection connection, String userID, String [] userInfo){
        try{
            String sqlQuery = "SELECT * FROM user_address a WHERE a.house_number =? AND a.postcode =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userInfo[3]);
            preparedStatement.setString(2, userInfo[6]);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                String insertSQL = "INSERT INTO user_address VALUES (?,?,?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(insertSQL);
                preparedStatement1.setString(1, userInfo[3]);
                preparedStatement1.setString(2, userInfo[4]);
                preparedStatement1.setString(3, userInfo[5]);
                preparedStatement1.setString(4, userInfo[6]);
                int rowsEffected = preparedStatement1.executeUpdate();
                System.out.println("address rows effected = " + rowsEffected);
            } else {
                String sqlUpdate = "UPDATE user_address a SET a.road_name=?, a.city_name=? " +
                                    "WHERE a.house_number=? AND a.postcode=?";
                preparedStatement = connection.prepareStatement(sqlUpdate);
                preparedStatement.setString(1, userInfo[4]);
                preparedStatement.setString(2, userInfo[5]);
                preparedStatement.setString(3, userInfo[3]);
                preparedStatement.setString(4, userInfo[6]);
                int rowsEffected = preparedStatement.executeUpdate();
                System.out.println("address rows effected = " + rowsEffected);
            }
            String sqlUpdate = "UPDATE users u SET u.first_name=?, u.last_name=?, u.email=?, u.house_number=?, u.postcode=? " +
                                    "WHERE u.user_ID=?";
            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, userInfo[0]);
            preparedStatement.setString(2, userInfo[1]);
            preparedStatement.setString(3, userInfo[2]);
            preparedStatement.setString(4, userInfo[3]);
            preparedStatement.setString(5, userInfo[6]);
            preparedStatement.setString(6, userID);
            int rowsEffected = preparedStatement.executeUpdate();
            System.out.println("user rows effected = " + rowsEffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String userHasBankDetails(Connection connection, String userID){
        try{
            String sqlQuery = "SELECT * FROM user_has_bank_details u WHERE u.user_ID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("card_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBankDetails(Connection connection, String userID, String [] bankInfo){
        if(userHasBankDetails(connection, userID) != null){
            try{
                String sqlUpdate = "UPDATE bank_details b SET b.card_NO=?, b.bank_card_name=?, b.expiry_date=?, b.security_code=? " +
                                    "WHERE b.card_ID =?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
                preparedStatement.setString(1, bankInfo[0]);
                preparedStatement.setString(2, bankInfo[1]);
                preparedStatement.setString(3, bankInfo[2]);
                preparedStatement.setString(4, bankInfo[3]);
                preparedStatement.setString(5, userHasBankDetails(connection, userID));
                preparedStatement.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            try{
                String cardID = UniqueUserIDGenerator.generateUniqueUserID();
                String insertSQL = "INSERT INTO bank_details VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, cardID);
                preparedStatement.setString(2, bankInfo[0]);
                preparedStatement.setString(3, bankInfo[1]);
                preparedStatement.setString(4, bankInfo[2]);
                preparedStatement.setString(5, bankInfo[3]);
                preparedStatement.executeUpdate();

                insertSQL = "INSERT INTO user_has_bank_details VALUES (?,?)";
                preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, cardID);
                preparedStatement.setString(2, userID);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String[]> filterUsers(Connection connection){
        ArrayList<String[]> filteredData = new ArrayList<>();
        try{
            String sqlQuery = "SELECT u.user_ID, u.first_name, u.last_name, u.user_role FROM users u WHERE u.user_role != 'Manager'";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String[] data = new String[4];
                data[0] = resultSet.getString("user_ID");
                data[1] = resultSet.getString("first_name");
                data[2] = resultSet.getString("last_name");
                data[3] = resultSet.getString("user_role");
                filteredData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredData;
    }

    public void roleRegister(Connection connection, String userID, String role){
        try{
            //String sqlQuery = "SELECT u.user_ID, u.first_name, u.last_name FROM users u WHERE u.user_role = 'Customer'";
            String sqlUpdate = "UPDATE users u SET u.user_role=? WHERE u.user_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduct(Connection connection, char productType, List<String> productInfo, List<Integer> partQuantity) throws SQLException{
        try {
            String insertSQL = "INSERT INTO product (product_code, product_name, brand, retail_price, uk_gauge, stock) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, productInfo.get(0));
            preparedStatement.setString(2, productInfo.get(1));
            preparedStatement.setString(3, productInfo.get(2));
            preparedStatement.setBigDecimal(4, new BigDecimal(productInfo.get(3)));
            preparedStatement.setString(5, productInfo.get(4));
            preparedStatement.setInt(6, Integer.parseInt(productInfo.get(5)));
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        switch (productType) {
            case 'S':
                try {
                    String insertSQL = "INSERT INTO train_set_parts (product_code) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String insertSQL = "INSERT INTO rolling_stock (product_code, era_code) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    preparedStatement.setString(2, productInfo.get(6));
                    int rowsTwoAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'L':
                try {
                    String insertSQL = "INSERT INTO train_set_parts (product_code) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String insertSQL = "INSERT INTO locomotive (product_code, era_code, dcc_code) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    preparedStatement.setString(2, productInfo.get(6));
                    preparedStatement.setString(3, productInfo.get(7));
                    int rowsTwoAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'C':
                try {
                    String insertSQL = "INSERT INTO train_set_parts (product_code) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String insertSQL = "INSERT INTO controller (product_code, is_digital) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    int option;
                    if (productInfo.get(6).equals("yes")) {
                        option = 1;
                    } else { option = 0;}
                    preparedStatement.setInt(2, option);
                    int rowsTwoAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'R':
                try {
                    String insertSQL = "INSERT INTO track_piece (product_code) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    int rowsTwoAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'P':
                try {
                    String insertSQL2 = "INSERT INTO train_set_parts (product_code) VALUES (?)";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(insertSQL2);
                    preparedStatement2.setString(1, productInfo.get(0));
                    int rows2affected = preparedStatement2.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }

                try {
                    String insertSQL = "INSERT INTO track_pack (product_code) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    int rowsTwoAffected = preparedStatement.executeUpdate();


                    for (int i = 0; i < partQuantity.size(); i++) {
                        insertSQL = "INSERT INTO track_pack_has_parts (track_piece_code, track_pack_code, quantity) VALUES (?, ?, ?)";
                        preparedStatement = connection.prepareStatement(insertSQL);
                        preparedStatement.setString(1, productInfo.get(6 + i));
                        preparedStatement.setString(2, productInfo.get(0));
                        preparedStatement.setInt(3, partQuantity.get(i) );
                        rowsTwoAffected = preparedStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }

                break;
            case 'M':
                try {
                    String insertSQL = "INSERT INTO train_set (product_code) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, productInfo.get(0));
                    int rowsTwoAffected = preparedStatement.executeUpdate();
                    for (int i = 0; i < partQuantity.size(); i++) {
                        insertSQL = "INSERT INTO track_set_has_parts (train_set_code, train_part_code, quantity) VALUES (?, ?, ?)";
                        preparedStatement = connection.prepareStatement(insertSQL);
                        preparedStatement.setString(1, productInfo.get(0));
                        preparedStatement.setString(2, productInfo.get(6 + i));
                        preparedStatement.setInt(3, partQuantity.get(i) );
                        rowsTwoAffected = preparedStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
        }
    }

    public void deleteProduct(String productCode, Connection connection) throws SQLException {
        char productType = productCode.charAt(0);

        switch (productType) {
            case 'S':
                try {
                    String deleteSQL = "DELETE FROM rolling_stock WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_has_parts WHERE train_part_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_parts WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM product WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'L':
                try {
                    String deleteSQL = "DELETE FROM locomotive WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_has_parts WHERE train_part_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_parts WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM product WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'C':
                try {
                    String deleteSQL = "DELETE FROM controller WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_has_parts WHERE train_part_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_parts WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM product WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'R':
                try {
                    String deleteSQL = "DELETE FROM track_pack_has_parts WHERE track_piece_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM track_piece WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM product WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'P':
                try {
                    String deleteSQL = "DELETE FROM train_set_has_parts WHERE train_part_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM track_pack_has_parts WHERE track_pack_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM track_pack WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set_parts WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM product WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'M':
                try {
                    String deleteSQL = "DELETE FROM train_set_has_parts WHERE train_set_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM train_set WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    String deleteSQL = "DELETE FROM product WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
        }
    }

    public List<String> associatedSets(String productCode, Connection connection) throws SQLException {
        char productType = productCode.charAt(0);
        List<String> boxed_set_codes = new ArrayList<String>();
        switch (productType) {
            case 'R':
                try {
                    String selectSQL = "SELECT track_pack_code FROM track_pack_has_parts WHERE track_piece_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    boxed_set_codes = new ArrayList<String>();
                    while (resultSet.next()) {
                        boxed_set_codes.add(resultSet.getString("track_pack_code"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            default:
                try {
                    String selectSQL = "SELECT train_set_code FROM train_set_has_parts WHERE train_part_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    boxed_set_codes = new ArrayList<String>();
                    while (resultSet.next()) {
                        boxed_set_codes.add(resultSet.getString("train_set_code"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
        }
        return boxed_set_codes;
    }

    public List<String> productSearch(String category, Connection connection) throws SQLException {
        char productType = 'S';
        switch (category) {
            case "rolling stock":
                productType = 'S';
                break;
            case "locomotive":
                productType = 'L';
                break;
            case "track piece":
                productType = 'R';
                break;
            case "track pack":
                productType = 'P';
                break;
            case "train set":
                productType = 'M';
                break;
            case "controller":
                productType = 'C';
                break;
        }
        List<String> boxed_set_codes = new ArrayList<String>();

        try {
            String selectSQL = "SELECT product_code, product_name FROM product WHERE product_code LIKE '" + productType + "%'";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            boxed_set_codes = new ArrayList<String>();
            while (resultSet.next()) {
                boxed_set_codes.add(resultSet.getString("product_code"));
                boxed_set_codes.add(resultSet.getString("product_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return boxed_set_codes;
    }

    public List<String> getProductDetails(String productCode, Connection connection) throws SQLException {
        char productType = productCode.charAt(0);
        List<String> productDetails = new ArrayList<String>();

        try {
            String selectSQL = "SELECT * FROM product WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, productCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                productDetails.add(resultSet.getString("product_code"));
                productDetails.add(resultSet.getString("product_name"));
                productDetails.add(resultSet.getString("brand"));
                productDetails.add((resultSet.getBigDecimal("retail_price")).toString());
                productDetails.add(resultSet.getString("uk_gauge"));
                productDetails.add(Integer.toString(resultSet.getInt("stock")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        switch (productType) {
            case 'C':
                try {
                    String selectSQL = "SELECT * FROM controller WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        int option = resultSet.getInt("is_digital");
                        String choice;
                        if (option == 1) {
                            choice = "yes";
                        } else {choice = "no";}
                        productDetails.add(choice);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'L':
                try {
                    String selectSQL = "SELECT * FROM locomotive WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        productDetails.add(resultSet.getString("era_code"));
                        productDetails.add(resultSet.getString("dcc_code"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'S':
                try {
                    String selectSQL = "SELECT * FROM rolling_stock WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        productDetails.add(String.valueOf(resultSet.getString("era_code")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'M':
                try {
                    String selectSQL = "SELECT * FROM train_set_has_parts WHERE train_set_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        productDetails.add(resultSet.getString("train_part_code"));
                        productDetails.add(Integer.toString(resultSet.getInt("quantity")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'P':
                try {
                    String selectSQL = "SELECT * FROM track_pack_has_parts WHERE track_pack_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, productCode);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        productDetails.add(resultSet.getString("track_piece_code"));
                        productDetails.add(Integer.toString(resultSet.getInt("quantity")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
        }

        return productDetails;
    }

    public int partCount(String productCode, Connection connection) throws SQLException {
        int partNum = 0;
        char productType = productCode.charAt(0);

        if (productType == 'M') {
            try {
                String selectSQL = "SELECT COUNT(*) FROM train_set_has_parts WHERE train_set_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, productCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                partNum = resultSet.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        } else if (productType == 'P') {
            try {
                String selectSQL = "SELECT COUNT(*) FROM track_pack_has_parts WHERE track_pack_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, productCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                partNum = resultSet.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return partNum;
    }

    public void updateProductDetails(List <String> newProductDetails, String productCode, Connection connection) throws SQLException {
        char productType = productCode.charAt(0);

        try {
            String updateSQL = "UPDATE product SET product_name=?, brand=?, retail_price=?, uk_gauge=?, stock=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, newProductDetails.get(1));
            preparedStatement.setString(2, newProductDetails.get(2));
            preparedStatement.setBigDecimal(3, new BigDecimal(newProductDetails.get(3)));
            preparedStatement.setString(4, newProductDetails.get(4));
            preparedStatement.setInt(5, Integer.valueOf(newProductDetails.get(5)));
            preparedStatement.setString(6, productCode);

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        switch (productType) {
            case 'C':
                try {
                    String updateSQL = "UPDATE controller SET is_digital=? WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                    int option;
                    if (newProductDetails.get(6).equals("yes")) {
                        option = 1;
                    } else {option = 0;}

                    preparedStatement.setInt(1, option);
                    preparedStatement.setString(2, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'L':
                try {
                    String updateSQL = "UPDATE locomotive SET era_code=?, dcc_code=? WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

                    preparedStatement.setString(1, newProductDetails.get(6));
                    preparedStatement.setString(2, newProductDetails.get(7));
                    preparedStatement.setString(3, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'S':
                try {
                    String updateSQL = "UPDATE rolling_stock SET era_code=? WHERE product_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

                    preparedStatement.setString(1, newProductDetails.get(6));
                    preparedStatement.setString(2, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            case 'M':
                try {
                    String deleteSQL = "DELETE FROM train_set_has_parts WHERE train_set_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                int itemCount = (newProductDetails.size()-6)/2;
                for (int i = 0; i < itemCount*2; i+= 2) {
                    try {
                        String insertSQL = "INSERT INTO train_set_has_parts (train_set_code, train_part_code, quantity) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        preparedStatement.setString(1, productCode);
                        preparedStatement.setString(2, newProductDetails.get(6 + i));
                        preparedStatement.setInt(3, Integer.parseInt(newProductDetails.get(6 + i + 1)));
                        int rowsTwoAffected = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
                break;
            case 'P':
                try {
                    String deleteSQL = "DELETE FROM track_pack_has_parts WHERE track_pack_code=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setString(1, productCode);

                    int rowsAffected = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                int itemCount2 = (newProductDetails.size()-6)/2;
                for (int i = 0; i < itemCount2*2; i+= 2) {
                    try {
                        String insertSQL = "INSERT INTO track_pack_has_parts (track_pack_code, track_piece_code, quantity) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        preparedStatement.setString(1, productCode);
                        preparedStatement.setString(2, newProductDetails.get(6 + i));
                        preparedStatement.setInt(3, Integer.parseInt(newProductDetails.get(6 + i + 1)));
                        int rowsTwoAffected = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
        }
    }

    public List<String> retrieveOrderQueue(Connection connection) throws SQLException {
        List<String> orderDetails = new ArrayList<String>();
        try {
            String selectSQL = "SELECT order_number, customer_ID, order_date, totalCost FROM orders WHERE order_status = ? ORDER BY order_date ASC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, "Confirmed");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                orderDetails.add(resultSet.getString("order_number"));
                orderDetails.add(resultSet.getString("customer_id"));
                orderDetails.add(String.valueOf(resultSet.getDate("order_date")));
                orderDetails.add(String.valueOf(resultSet.getBigDecimal("totalCost")));

                try {
                    selectSQL = "SELECT * FROM users WHERE user_id=?";
                    preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, orderDetails.get(1));
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        orderDetails.add(resultSet.getString("first_name"));
                        orderDetails.add(resultSet.getString("last_name"));
                        orderDetails.add(resultSet.getString("email"));
                        orderDetails.add(resultSet.getString("house_number"));
                        orderDetails.add(resultSet.getString("postcode"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    selectSQL = "SELECT * FROM user_address WHERE postcode=? AND house_number=?";
                    preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setString(1, orderDetails.get(8));
                    preparedStatement.setString(2, orderDetails.get(7));
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        orderDetails.add(resultSet.getString("road_name"));
                        orderDetails.add(resultSet.getString("city_name"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    selectSQL = "SELECT * FROM order_line WHERE order_number=?";
                    preparedStatement = connection.prepareStatement(selectSQL);
                    preparedStatement.setInt(1 ,Integer.parseInt(orderDetails.get(0)));
                    resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        orderDetails.add(String.valueOf(resultSet.getInt("order_line_number")));
                        orderDetails.add(resultSet.getString("product_code"));
                        orderDetails.add(String.valueOf(resultSet.getInt("product_num")));
                        orderDetails.add(String.valueOf(resultSet.getBigDecimal("line_cost")));
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return orderDetails;
    }

    public boolean inStock(Connection connection) throws SQLException {
        int orderNum = 0;
        List<String> products = new ArrayList<String>();
        List<Integer> quantity = new ArrayList<Integer>();
        try {
            String selectSQL = "SELECT order_number FROM orders WHERE order_status = ? ORDER BY order_date ASC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, "Confirmed");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                orderNum = resultSet.getInt("order_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        try {
            String selectSQL = "SELECT product_code, product_num FROM order_line WHERE order_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, orderNum);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(resultSet.getString("product_code"));
                quantity.add(resultSet.getInt("product_num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        boolean available = true;
        for (int i = 0; i < products.size(); i++) {
            try {
                String selectSQL = "SELECT stock FROM product WHERE product_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, products.get(i));
                ResultSet resultSet = preparedStatement.executeQuery();
                int result = 999;
                if (resultSet.next()) {
                    result = resultSet.getInt("stock");
                }
                if (result < quantity.get(i)) {
                    available = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return available;
    }

    public void staffDeleteQueueOrder(Connection connection) throws SQLException{
        int orderNumber = 0;
        try{
            String selectSQL = "SELECT order_number FROM orders WHERE order_status = ? ORDER BY order_date ASC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, "Confirmed");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                orderNumber = resultSet.getInt("order_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            String deleteSQL = "DELETE FROM order_line WHERE order_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, orderNumber);
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            String deleteSQL = "DELETE FROM orders WHERE order_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, orderNumber);
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void fulfillOrder(Connection connection) throws SQLException{
        int orderNumber = 0;
        try{
            String selectSQL = "SELECT order_number FROM orders WHERE order_status = ? ORDER BY order_date ASC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, "Confirmed");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                orderNumber = resultSet.getInt("order_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            String updateSQL = "UPDATE orders SET order_status=? WHERE order_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1, "Fulfilled");
            preparedStatement.setInt(2, orderNumber);
            int rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        List<String> productCodes = new ArrayList<String>();
        List<Integer> productQuantity = new ArrayList<Integer>();

        try {
            String selectSQL = "SELECT product_code, product_num FROM order_line WHERE order_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, orderNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productCodes.add(resultSet.getString("product_code"));
                productQuantity.add(resultSet.getInt("product_num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        for (int i = 0; i < productQuantity.size(); i++) {
            try {
                String updateSQL = "UPDATE product SET stock = stock - ? WHERE product_code =?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                preparedStatement.setInt(1, productQuantity.get(i));
                preparedStatement.setString(2, productCodes.get(i));
                int rowsAffected = preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public List<String> getUserOrders(String user_id, Connection connection) throws SQLException{
        List<String> orders = new ArrayList<String>();
        try {
            String selectSQL = "SELECT order_number, totalCost FROM orders WHERE customer_ID=? AND order_status=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, "Pending");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(String.valueOf(resultSet.getInt("order_number")));
                orders.add(String.valueOf(resultSet.getBigDecimal("totalCost")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return orders;

    }

    public void addToOrder(int orderNumber, String productCode, int quantity, Connection connection) throws SQLException {
        BigDecimal productPrice = new BigDecimal(0);
        try {
            String selectSQL = "SELECT retail_price FROM product WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, productCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                productPrice = resultSet.getBigDecimal("retail_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        int newOrderLineNum = 1;
        try {
            String selectSQL = "SELECT order_line_number FROM order_line WHERE order_number=? ORDER BY order_line_number DESC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, orderNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newOrderLineNum = resultSet.getInt("order_line_number") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        try {
            String insertSQL = "INSERT INTO order_line (order_number, order_line_number,product_code,product_num, line_cost) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, orderNumber);
            preparedStatement.setInt(2, newOrderLineNum);
            preparedStatement.setString(3, productCode);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setBigDecimal(5, new BigDecimal(quantity).multiply(productPrice));
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            String updateSQL = "UPDATE orders SET totalCost = totalCost + ? WHERE order_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setBigDecimal(1, productPrice.multiply(new BigDecimal(quantity)));
            preparedStatement.setInt(2, orderNumber);
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void addToNewOrder(String user_id, String productCode, int quantity, Connection connection) throws SQLException {
        int order_number = 1;
        try {
            String selectSQL = "SELECT order_number FROM orders ORDER BY order_number DESC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                order_number = resultSet.getInt("order_number") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println(order_number);

        BigDecimal productPrice = new BigDecimal(0);
        try {
            String selectSQL = "SELECT retail_price FROM product WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, productCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                productPrice = resultSet.getBigDecimal("retail_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            String insertSQL = "INSERT INTO orders (order_number, customer_ID, order_status, order_date, totalCost) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, order_number);
            preparedStatement.setString(2, user_id);
            preparedStatement.setString(3, "Pending");
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.setBigDecimal(5, new BigDecimal(0));
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(order_number);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        DatabaseOperations databaseOperations = new DatabaseOperations();
        try {
            System.out.println(order_number);
            databaseOperations.addToOrder(order_number, productCode, quantity, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public boolean productExists(String pC, Connection connection) throws  SQLException {
        boolean result = false;
        try {
            String selectSQL = "SELECT * FROM product WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, pC);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }
}
