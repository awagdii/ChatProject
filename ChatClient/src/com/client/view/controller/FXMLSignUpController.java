/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import com.client.util.Utilities;
import static com.client.util.Utilities.userInterface;
import static com.client.view.controller.ClientLogin.serverInterface;
import com.server.interfaces.UserInterface;
import com.server.model.Server;
import com.server.model.User;

//import com.server.interfaces.UserInterface;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author nessma
 */
public class FXMLSignUpController implements Initializable {

    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public RadioButton maleRadio;
    @FXML
    public RadioButton femaleRadio;
    @FXML
    public Label validationLabel;
    @FXML
    public Button signUpButton;
    @FXML
    public ComboBox country;
    // public TextField country;
    public User user = new User();
    Pattern pattern;
    Matcher matcher;
    String output;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        country.getItems().addAll("Egypt", "USA", "KSA", "Algeria", "Australia", "Bahrain", "UAE", "Brazil", "China", "Greece", "Hong Kong", "Iran", "Japan");
        ToggleGroup radioGroup = new ToggleGroup();
        maleRadio.setToggleGroup(radioGroup);
        femaleRadio.setToggleGroup(radioGroup);
        signUpButton.setStyle("-fx-effect: dropshadow( one-pass-box , rgba(0,0,0,0.9) , 1, 0.0 , 0 , 1 ); -fx-background-color: \n"
                + "#000000,\n"
                + "linear-gradient(#7ebcea, #2f4b8f),\n"
                + "linear-gradient(#426ab7, #263e75),\n"
                + "linear-gradient(#395cab, #223768);\n"
                + "-fx-background-radius: 30;"
                + "-fx-background-insets: 0,1,2,3,0;"
                + "-fx-text-fill: black;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 10px;"
                + "-fx-padding: 10 20 10 20;");
        firstNameField.setStyle("-fx-background-radius: 30;");
        lastNameField.setStyle("-fx-background-radius: 30;");
        passwordField.setStyle("-fx-background-radius: 30;");
        emailField.setStyle("-fx-background-radius: 30;");
        maleRadio.setStyle("-fx-background-radius: 30;");
        femaleRadio.setStyle("-fx-background-radius: 30;");
        country.setStyle("-fx-background-radius: 30;");
        country.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                output = (String) country.getSelectionModel().getSelectedItem().toString();

            }
        });
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                validationLabel.setText("");

                if (validateEmail(emailField.getText()) && passwordField.getText().length() != 0 && output != null && validateFirstName(firstNameField.getText()) && validateLastName(lastNameField.getText()) && ((maleRadio.isSelected()) || (femaleRadio.isSelected()))) {
                    try {
                        if (Utilities.userInterface.checkMail(emailField.getText())) {
                            user.seteMail(emailField.getText());
                            user.setPassword(passwordField.getText());
                            user.setStatus(1);
                            user.setCountry(output);
                            user.setFirstName(firstNameField.getText());
                            user.setLastName(lastNameField.getText());

                            if (maleRadio.isSelected()) {
                                user.setGender("male");
                            }
                            if (femaleRadio.isSelected()) {
                                user.setGender("female");
                            }
                            System.out.println(user.getGender());
                            Server server = new Server(1, 1);
                            boolean checkServer = serverInterface.checkServer(server);
                            if (checkServer) {
                                boolean added = userInterface.addUser(user);
                                if (added) {
                                    //  ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
                                    TrayNotification successNotification = new TrayNotification("Success", "Registered Successfully", NotificationType.SUCCESS);
                                successNotification.setAnimationType(AnimationType.SLIDE);
                                successNotification.showAndDismiss(Duration.millis(3000));
                                    new ClientLogin();
                                } else {
                                    validationLabel.setText("Email Already Exists");
                                }

                            } else {
                                TrayNotification errorNotification = new TrayNotification("Error", "Server is closed , Please wait till its opened", NotificationType.ERROR);
                                errorNotification.setAnimationType(AnimationType.SLIDE);
                                errorNotification.showAndDismiss(Duration.millis(3000));
                            }
                        }

                    } catch (RemoteException ex) {
                        Logger.getLogger(FXMLSignUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (!validateEmail(emailField.getText())) {
                    validationLabel.setText("Enter Valid Email ");
                }
                if (!(passwordField.getText().length() != 0)) {

                    validationLabel.setText("Enter Valid password ");
                }
                if (!validateFirstName(firstNameField.getText())) {

                    validationLabel.setText("Enter Valid First Name ");
                }
                if (!validateLastName(lastNameField.getText())) {

                    validationLabel.setText("Enter Valid Last Name ");
                }
                if (!(maleRadio.isSelected()) && !(femaleRadio.isSelected())) {
                    validationLabel.setText("Enter Your Gender");
                }
                if (output == null) {
                    validationLabel.setText("Enter your Country");
                }

            }

        });

        try {

            Registry registry = LocateRegistry.getRegistry(9000);
            userInterface = (UserInterface) registry.lookup("UserService");

        } catch (Exception ex) {
            //validationLabel.setText("this Email already exist ");
            Logger.getLogger(FXMLSignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean validateFirstName(String firstName) {

        if (firstName.isEmpty() || firstName.indexOf(" ") > 0 || containDigits(firstName) || firstName.length() < 2 || firstName.length() > 10 || firstName.length() == 0) {
            return false;
        } else {
            return true;
        }

    }

    public boolean validateLastName(String lastName) {

        if (lastName.isEmpty() || lastName.indexOf(" ") > 0 || containDigits(lastName) && lastName.length() < 2 || lastName.length() > 10 || lastName.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean containDigits(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (Character.isDigit(name.indexOf(i))) {
                return true;
            }

        }
        return false;
    }

    public boolean validatePassword(String password) {

        final String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        pattern = Pattern.compile(passwordPattern);

        matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateEmail(String email) {

        final String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

        pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);

        matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
