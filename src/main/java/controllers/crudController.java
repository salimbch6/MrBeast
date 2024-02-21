package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class crudController implements Initializable {
    @FXML
    private TableView<User> userT;
    @FXML
    private ImageView userIconView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;

    private UserServices userServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up user icon
        File userFile = new File("target/classes/user icon.png");
        Image userImage = new Image(userFile.toURI().toString());
        userIconView.setImage(userImage);

        // Instantiate UserServices
        userServices = new UserServices();



        idColumn.setCellValueFactory(new PropertyValueFactory<>("account_id"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        populateTable();

    }

    private void populateTable() {
        // Clear existing items in the TableView
        userT.getItems().clear();

        // Get database connection
        MyDataBase connectNow = new MyDataBase();
        Connection connectDB = connectNow.getconn();

        try {
            // Retrieve user data from the database
            ResultSet resultSet = userServices.getAllUsers(connectDB);

            // Add users to the TableView
            while (resultSet.next()) {
                int account_id = resultSet.getInt("account_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(account_id, firstname, lastname, username, password);
                userT.getItems().add(user);
            }

            // Debug output
            if (!userT.getItems().isEmpty()) {
                System.out.println("Users successfully retrieved and added to TableView.");
            } else {
                System.out.println("No users found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        // Get the selected user
        User selectedUser = userT.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Get database connection
            MyDataBase connectNow = new MyDataBase();
            Connection connectDB = connectNow.getconn();

            try {
                // Delete the selected user from the database
                userServices.deleteUser(connectDB, selectedUser.getAccount_id());
                // Remove the user from the TableView
                userT.getItems().remove(selectedUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // No user selected, display an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }
    @FXML
    private void updateButtonClicked(ActionEvent event) {
        // Get the selected user
        User selectedUser = userT.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                // Load the register.fxml file and pass the selected user's data
                FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
                Parent root = loader.load();

                // Get the controller associated with the register.fxml file
                RegisterController registerController = loader.getController();

                // Pass the selected user's data to the register controller
                registerController.initData(selectedUser);

                // Show the register stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
