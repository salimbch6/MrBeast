package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import services.UserServices;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView securityImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;

    private UserServices userService;
    private User selectedUser; // Store the selected user data

    private Stage currentStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File securityFile = new File("target/classes/security.png");
        Image securityImage = new Image(securityFile.toURI().toString());
        securityImageView.setImage(securityImage);

        // Initialize label messages to be empty
        registrationMessageLabel.setText("");
        confirmPasswordLabel.setText("");

        // Instantiate UserService
        userService = new UserServices();

        // Initialize selectedUser to null
        selectedUser = null;
    }

    // Method to set the selected user's data
    public void initData(User user) {
        selectedUser = user;
        // Set the fields with the selected user's data
        firstnameTextField.setText(selectedUser.getFirstname());
        lastnameTextField.setText(selectedUser.getLastname());
        usernameTextField.setText(selectedUser.getUsername());
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void registerButtonOnAction(ActionEvent event) {
        String setPassword = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if password fields are empty
        if (setPassword.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("Please fill in all fields");
            return; // Exit the method
        }

        // Check if passwords match
        if (setPassword.equals(confirmPassword)) {
            // Update the user's data
            updateUser();
            confirmPasswordLabel.setText("User updated successfully");

            // Close the register window
            closeRegisterWindow();
        } else {
            confirmPasswordLabel.setText("Passwords do not match");
            registrationMessageLabel.setText(""); // Clear any previous success message
        }
    }

    private void closeRegisterWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void closeButtonOnAction(ActionEvent event) {
        currentStage.close(); // Close the register window
    }

    public void updateUser() {
        if (selectedUser != null) {
            String firstname = firstnameTextField.getText();
            String lastname = lastnameTextField.getText();
            String username = usernameTextField.getText();
            String password = setPasswordField.getText();

            try {
                // Update the user's data
                selectedUser.setFirstname(firstname);
                selectedUser.setLastname(lastname);
                selectedUser.setUsername(username);
                selectedUser.setPassword(password);

                // Update the user in the database
                userService.updateUser(selectedUser);
                registrationMessageLabel.setText("User updated successfully");
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }
    }
}
