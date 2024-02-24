package services;

import models.User;

import java.sql.*;

public class UserServices {
    public void registerUser(Connection connection, String firstname, String lastname, String username, String password) throws SQLException {
        String insertFields = "INSERT INTO user_account(firstname, lastname, username, password) VALUES ('";
        String insertValues = firstname + "','" + lastname + "','" + username + "','" + password + "')";
        String insertToRegister = insertFields + insertValues;

        try {
            PreparedStatement statement = connection.prepareStatement(insertToRegister);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean validateLogin(Connection connection, String username, String password) throws SQLException {
        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(verifyLogin);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet queryResult = statement.executeQuery();
            if (queryResult.next()) {
                int count = queryResult.getInt(1);
                return count == 1;
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    public ResultSet getAllUsers(Connection connection) throws SQLException {
        String query = "SELECT * FROM user_account";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteUser(Connection connection, int id) throws SQLException {
        String req = "DELETE FROM user_account WHERE account_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateUser(Connection connection, User user) throws SQLException {
        String updateQuery = "UPDATE user_account SET firstname=?, lastname=?, username=?, password=? WHERE account_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getAccount_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    public ResultSet searchUsers(Connection connection, String query) throws SQLException {
        String sql = "SELECT * FROM user_account WHERE firstname LIKE ? OR lastname LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + query + "%");
        statement.setString(2, "%" + query + "%");
        return statement.executeQuery();
    }

}
