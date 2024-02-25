package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.UserServices;
import utils.MyDataBase;

import java.io.*;
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
    @FXML
    private Button imageButton;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label imageLabel;

    private UserServices userService;
    private MyDataBase myDataBase; // Database connection
    FileChooser fileChooser = new FileChooser();
    private File selectedFile;



    private Stage currentStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File securityFile = new File("target/classes/security.png");
        Image securityImage = new Image(securityFile.toURI().toString());
        securityImageView.setImage(securityImage);

        //fileChooser.setInitialDirectory(new File(""));

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
        String imageString=imageLabel.getText();

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



        try {
            // Register the user
            userService.registerUser(myDataBase.getconn(), firstname, lastname, username, setPassword,imageString);

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