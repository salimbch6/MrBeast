package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;
import utils.EncryptionUtils; // Import the EncryptionUtils class
import utils.Session;

import java.net.MalformedURLException;
import java.net.URL;
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
    private TableColumn<User, String> imageColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private Hyperlink signoutLink;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private Circle imageView;


    private UserServices userServices;



    MyDataBase connectNow = new MyDataBase();
    Connection connectDB = connectNow.getconn();
    URL imageUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // Set up user icon

        //File userFile = new File("target/classes/user icon.png");
        //Image userImage = new Image(userFile.toURI().toString());
        //userIconView.setImage(userImage);
        signoutLink.setText("Sign out");

        // Instantiate UserServices
        userServices = new UserServices();
        User u = userServices.getUserById_Account(Session.getAccount_id()); //star AZIZ bech tekhedh user mel session
        usernameLabel.setText(u.getUsername());
        try {
            imageUrl = new URL("http://localhost/images/"+u.getProfilePic());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image images = new Image(imageUrl.toString());
        imageView.setFill(new ImagePattern(images));




        idColumn.setCellValueFactory(new PropertyValueFactory<>("account_id"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("profilePic"));



        // Update password column to display encrypted passwords
        passwordColumn.setCellValueFactory(data -> {
            String encryptedPassword = EncryptionUtils.encrypt(data.getValue().getPassword());
            return new SimpleStringProperty(encryptedPassword);
        });

        populateTable();
    }

    void populateTable() {
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
                String img = resultSet.getString("profilePic");


                User user = new User(account_id,1, firstname, lastname, username, password,img);
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
    public void updateButtonOnAction(ActionEvent actionEvent) {
        // Get the selected user
        User selectedUser = userT.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                // Close the current window
                Stage stage = (Stage) updateButton.getScene().getWindow();
                stage.close();

                // Load the update.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/update.fxml"));
                Parent root = loader.load();

                // Get the controller associated with the update.fxml
                updateController updateController = loader.getController();
                // Pass the selected user's data to the updateController
                System.out.println(selectedUser);
                updateController.initData(selectedUser);
                // Create a new stage for the update.fxml window
                Stage updateStage = new Stage();
                updateStage.setScene(new Scene(root));
                updateStage.setTitle("Update User");

                // Show the update.fxml window
                updateStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // No user selected, display an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to update.");
            alert.showAndWait();
        }
    }

    public void singoutOnAction(ActionEvent actionEvent) {
        try {
            // Close the current window
            Stage stage = (Stage) signoutLink.getScene().getWindow();
            stage.close();

            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Create a new stage for the login.fxml window
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");

            // Show the login.fxml window
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void searchOnAction(ActionEvent actionEvent) {
        String query = searchTextField.getText().trim();
        if (!query.isEmpty()) {
            // Perform search based on the query
            searchUsers(query);
        } else {
            // If search query is empty, reset the TableView to show all users
            populateTable();
        }
    }

    private void searchUsers(String query) {
        // Clear existing items in the TableView
        userT.getItems().clear();

        // Get database connection
        MyDataBase connectNow = new MyDataBase();
        Connection connectDB = connectNow.getconn();

        try {
            // Retrieve user data from the database based on search query
            ResultSet resultSet = userServices.searchUsers(connectDB, query);

            // Add matching users to the TableView
            while (resultSet.next()) {
                int account_id = resultSet.getInt("account_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String img = resultSet.getString("profilePic");


                User user = new User(account_id,1, firstname, lastname, username, password,img);
                userT.getItems().add(user);
            }

            // Debug output
            if (!userT.getItems().isEmpty()) {
                System.out.println("Users matching the search query successfully retrieved and added to TableView.");
            } else {
                System.out.println("No users found matching the search query.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
