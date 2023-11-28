package project.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
}
