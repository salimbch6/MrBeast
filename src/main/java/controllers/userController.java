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
import utils.Session;

import java.io.File;
import java.io.IOException;
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
                int isValidLogin = userServices.validateLogin(connectDB, usernameTextField.getText(), enterPasswordField.getText());
                if (isValidLogin != -1) {
                    Session.setAccount_id(isValidLogin);
                    System.out.println(Session.getAccount_id());
                    User u = userServices.getUserById_Account(isValidLogin);
                    System.out.println(u);
                    if (u.getId_role()==1){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientInterface.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage clientStage = new Stage();
                        clientStage.setTitle("Client Management");
                        clientStage.setScene(scene);
                        clientStage.show();
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        currentStage.close();
                    }
                    else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crud.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage crudStage = new Stage();
                        crudStage.setTitle("Admin Management");
                        crudStage.setScene(scene);
                        crudStage.show();
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        currentStage.close();
                         }
                } else {
                    loginMessageLabel.setText("Invalid login. Please try again");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
    @FXML
    private void openRegisterPage(ActionEvent event) {
        createAccountForm();
    }
}
