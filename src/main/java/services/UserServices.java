package services;

import models.User;
import utils.MyDataBase;

import java.sql.*;

public class UserServices {
    Connection cnx = MyDataBase.getInstance().getconn();
    public void registerUser(Connection connection, String firstname, String lastname, String username, String password,String img) throws SQLException {
        String insertFields = "INSERT INTO user_account(id_role, firstname, lastname, username, password,profilePic) VALUES (?, ?, ?, ?, ?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertFields);
            // Assuming "client" role has id 1 (You may need to adjust this based on your role table)
            statement.setInt(1, 1); // Set role id to "client"
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, username);
            statement.setString(5, password);
            statement.setString(6, img);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
        public int validateLogin(Connection connection, String username, String password) throws SQLException {
        String verifyLogin = "SELECT * FROM user_account WHERE username = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(verifyLogin);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet queryResult = statement.executeQuery();
            if (queryResult.next()) {
                //int count = queryResult.getInt(1);
                return queryResult.getInt("account_id");
            }
        } catch (SQLException e) {
            throw e;
        }
        return -1;
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
    public User getUserById_Account( int id_account)  {
        User b = new User();


        try {

            String req = "SELECT * FROM user_account WHERE account_id= "+id_account;
            //Statement st = cnx.createStatement();
            Statement st =cnx.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(req);
            rs.beforeFirst();
            rs.next();
            b.setAccount_id(rs.getInt(1));
            b.setId_role(rs.getInt(2));
            b.setFirstname(rs.getString(3));
            b.setLastname(rs.getString(4));
            b.setUsername(rs.getString(5));
            b.setPassword(rs.getString(6));
            b.setProfilePic(rs.getString(7));




        } catch (SQLException ex) {
            ex.printStackTrace();
        }



        return b;
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
        String updateQuery = "UPDATE user_account SET firstname=?, lastname=?, username=?, password=?,profilePic=? WHERE account_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(6, user.getAccount_id());
            preparedStatement.setString(5,user.getProfilePic());
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
