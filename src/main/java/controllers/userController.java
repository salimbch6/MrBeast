package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.UserServices;
import utils.MyDataBase;
import models.User;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class userController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;

    @FXML
    private PasswordField enterPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;
    @FXML
    private Hyperlink registerLink;

    private UserServices userServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("target/classes/gymlogin.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("target/classes/login icon.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
        registerLink.setText("Don't have an account? Register here");

        // Instantiate UserServices
        userServices = new UserServices();
    }

    public void CancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
            try {
                MyDataBase connectNow = new MyDataBase();
                Connection connectDB = connectNow.getconn();
                boolean isValidLogin = userServices.validateLogin(connectDB, usernameTextField.getText(), enterPasswordField.getText());
                if (isValidLogin) {
                    loginMessageLabel.setText("Congratulations!");
                    openCrudWindow(); // Call the method to open CRUD window
                } else {
                    loginMessageLabel.setText("Invalid login. Please try again");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            loginMessageLabel.setText("Please enter Username and Password");
        }
    }


    public void createAccountForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage registerstage = new Stage();
            registerstage.setTitle("Login");
            registerstage.setScene(scene);
            registerstage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCrudWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crud.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage crudStage = new Stage();
            crudStage.setTitle("User Management");
            crudStage.setScene(scene);
            crudStage.show();
            // Close the current login window
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openRegisterPage(ActionEvent event) {
        createAccountForm();
    }
}
