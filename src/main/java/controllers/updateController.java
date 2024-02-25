package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserServices;
import utils.MyDataBase;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class updateController implements Initializable {

    @FXML
    private ImageView updateImageView;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button imageButton;
    @FXML
    private Label imageLabel;
    @FXML
    private ImageView profileImageView;


    private User selectedUser;
    private File selectedFile;
    private UserServices userServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File updateFile = new File("target/classes/update icon.png");
        Image updateImage = new Image(updateFile.toURI().toString());
        updateImageView.setImage(updateImage);
        userServices = new UserServices(); // Instantiate UserServices
    }

    // Method to receive selected user's data from crudController
    public void initData(User user) throws MalformedURLException {
        selectedUser = user;
        URL imageUrl;
        // Populate the form fields with selected user's data
        firstnameTextField.setText(selectedUser.getFirstname());
        lastnameTextField.setText(selectedUser.getLastname());
        usernameTextField.setText(selectedUser.getUsername());
        setPasswordField.setText(selectedUser.getPassword());
        confirmPasswordField.setText(selectedUser.getPassword());
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
        selectedUser.setFirstname(firstnameTextField.getText());
        selectedUser.setLastname(lastnameTextField.getText());
        selectedUser.setUsername(usernameTextField.getText());
        selectedUser.setPassword(setPasswordField.getText());
        selectedUser.setPassword(confirmPasswordField.getText());
        selectedUser.setProfilePic(imageLabel.getText());
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
            userServices.updateUser(connectDB, selectedUser); // Update user data

            // Close the update window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

            // Load the crud.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crud.fxml"));
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
    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            // Close the current window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

            // Load the crud.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crud.fxml"));
            Parent root = loader.load();

            // Create a new stage for the crud.fxml window
            Stage crudStage = new Stage();
            crudStage.setScene(new Scene(root));
            crudStage.setTitle("CRUD Interface");

            // Show the crud.fxml window
            crudStage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
}
