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
import services.UserServices;
import utils.MyDataBase;

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
    private MyDataBase myDataBase; // Database connection

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

        // Get instance of database connection
        myDataBase = MyDataBase.getInstance();
    }

    public void registerButtonOnAction(ActionEvent event) {
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String setPassword = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if any fields are empty
        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || setPassword.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("Please fill in all fields");
            return; // Exit the method
        }

        // Check if passwords match
        if (!setPassword.equals(confirmPassword)) {
            confirmPasswordLabel.setText("Passwords do not match");
            registrationMessageLabel.setText(""); // Clear any previous success message
            return; // Exit the method
        }

        // Check password length
        if (setPassword.length() < 8) {
            confirmPasswordLabel.setText("Password must be at least 8 characters long");
            registrationMessageLabel.setText(""); // Clear any previous success message
            return; // Exit the method
        }

        // Check password complexity
        if (!isPasswordComplex(setPassword)) {
            confirmPasswordLabel.setText("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
            registrationMessageLabel.setText(""); // Clear any previous success message
            return; // Exit the method
        }

        try {
            // Register the user
            userService.registerUser(myDataBase.getconn(), firstname, lastname, username, setPassword);

            // Show success message
            registrationMessageLabel.setText("User registered successfully");

            // Close the register window
            closeRegisterWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException appropriately
            registrationMessageLabel.setText("Error: Unable to register user. Please try again.");
        }
    }

    private boolean isPasswordComplex(String password) {
        // Password must contain at least one uppercase letter, one lowercase letter, and one digit
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*");
    }

    private void closeRegisterWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void closeButtonOnAction(ActionEvent event) {
        // Close the register window
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
