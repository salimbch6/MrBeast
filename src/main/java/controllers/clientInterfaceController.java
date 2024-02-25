package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.EncryptionUtils;
import utils.MyDataBase;
import utils.Session;
import javafx.scene.control.Label;


import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class clientInterfaceController implements Initializable {
    @FXML
    private ImageView clientProfilePic;
    @FXML
    private Label clientLabel;
    @FXML
    private Hyperlink signoutLink;
    private UserServices userServices;




    MyDataBase connectNow = new MyDataBase();
    Connection connectDB = connectNow.getconn();
    URL imageUrl;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Instantiate UserServices
        userServices = new UserServices();
        User u = userServices.getUserById_Account(Session.getAccount_id()); //star AZIZ bech tekhedh user mel session
        clientLabel.setText(u.getUsername());
        signoutLink.setText("Sign out");
        try {
            imageUrl = new URL("http://localhost/images/"+u.getProfilePic());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image images = new Image(imageUrl.toString());
        clientProfilePic.setImage(images);
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


}
