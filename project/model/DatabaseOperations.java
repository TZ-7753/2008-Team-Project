package project.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import project.util.UniqueUserIDGenerator;

public class DatabaseOperations {

    public String verifyLogin(Connection connection, String email, String hashedPassword) {
        String returnMessage = "";
        try {
            String sqlQuery = "SELECT u.user_ID,u.password_hash FROM users u WHERE u.email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPasswordDB = resultSet.getString("password_hash");
                if (hashedPasswordDB.equals(hashedPassword)) {
                    returnMessage = "UserID: " + resultSet.getString("user_ID");
                } else {
                    returnMessage = "Incorrect Password!";
                } 
            } else {
                returnMessage = "User with Email: " + email + " does not exist!";
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
}
