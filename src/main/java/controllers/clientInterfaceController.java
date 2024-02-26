package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.EncryptionUtils;
import utils.MyDataBase;
import utils.Session;


import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class clientInterfaceController implements Initializable {

    @FXML
    private Label clientLabel;
    @FXML
    private Hyperlink signoutLink;
    @FXML
    private Circle clientProfilePic;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private javafx.scene.control.TextField firstnameTextField;
    @FXML
    private javafx.scene.control.TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button editProfileButton;
    @FXML
    private Button imageButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label imageLabel;
    @FXML
    private ImageView profileImageView;
    private User u;

    private UserServices userServices;
    //private User selectedUser;
    private File selectedFile;




    MyDataBase connectNow = new MyDataBase();
    Connection connectDB = connectNow.getconn();
    URL imageUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userServices = new UserServices();
         u = userServices.getUserById_Account(Session.getAccount_id());

        // Instantiate UserServices
        userServices = new UserServices();
         //star AZIZ bech tekhedh user mel session
        clientLabel.setText(u.getUsername());
        signoutLink.setText("Sign out");
        try {
            imageUrl = new URL("http://localhost/images/"+u.getProfilePic());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image images = new Image(imageUrl.toString());
        clientProfilePic.setFill(new ImagePattern(images));
    }
    public void signoutOnAction(ActionEvent actionEvent) {
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
    public void initData(User user) throws MalformedURLException {
        // = user;
        URL imageUrl;
        // Populate the form fields with selected user's data
        firstnameTextField.setText(u.getFirstname());
        lastnameTextField.setText(u.getLastname());
        usernameTextField.setText(u.getUsername());
        setPasswordField.setText(u.getPassword());
        confirmPasswordField.setText(u.getPassword());
        imageLabel.setText(user.getProfilePic());
        try {
            imageUrl = new URL("http://localhost/images/"+user.getProfilePic());
            System.out.println(imageUrl);
            Image images = new Image(imageUrl.toString());
            profileImageView.setImage(images);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Password fields can be left empty for security reasons
    }
    @FXML
    public void saveButtonOnAction(ActionEvent actionEvent) {
        // Update selected user's data with the modified data from the form
        u.setFirstname(firstnameTextField.getText());
        u.setLastname(lastnameTextField.getText());
        u.setUsername(usernameTextField.getText());
        u.setPassword(setPasswordField.getText());
        u.setPassword(confirmPasswordField.getText());
        u.setProfilePic(imageLabel.getText());
        // You might want to handle password updates here as well

        // Get database connection
        Connection connectDB = MyDataBase.getInstance().getconn();
        String htdocsPath = "C:/xampp/htdocs/images/";
        File destinationFile = new File(htdocsPath + imageLabel.getText().replaceAll("\\s", ""));
        if(selectedFile!=null){
            try (InputStream in = new FileInputStream(selectedFile);
                 OutputStream out = new FileOutputStream(destinationFile)) {
                byte[] buf = new byte[8192];
                int length;
                while ((length = in.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }} catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Update user data in the database
        try {
            UserServices userServices = new UserServices(); // Instantiate UserServices
            userServices.updateUser(connectDB, u); // Update user data

            // Close the update window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

            // Load the crud.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientInterface.fxml"));
            Parent root = loader.load();

            // Create a new stage for the crud.fxml window
            Stage crudStage = new Stage();
            crudStage.setScene(new Scene(root));
            crudStage.setTitle("CRUD Interface");

            // Show the crud.fxml window
            crudStage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle any exceptions
        }
    }


    @FXML
    private void imageOnMouseClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.JPG", "*.gif"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            //maj
            imageLabel.setText(selectedFile.getName().replaceAll("\\s", ""));
            try {
                Image images = new Image("file:"+selectedFile.getPath().toString());
                profileImageView.setImage(images);
                System.out.println(selectedFile.getPath().toString());
            } catch (Exception ex) {
                System.out.println(ex);
            }

        }
    }

    @FXML
    public void editProfileOnAction(ActionEvent actionEvent) {
        try {
            // Close the current window
            Stage stage = (Stage) editProfileButton.getScene().getWindow();
            stage.close();

            // Load the clientInterface.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientInterface.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the clientInterface.fxml
            clientInterfaceController clientController = loader.getController();

            // Pass the selected user's data to the clientController
            System.out.println(u);
            clientController.initData(u);

            // Create a new stage for the clientInterface.fxml window
            Stage clientStage = new Stage();
            clientStage.setScene(new Scene(root));
            clientStage.setTitle("Client Interface");

            // Show the clientInterface.fxml window
            clientStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
