/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.server.dao.UserOperations;
import com.server.model.User;
import com.server.util.Utilities;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminAddNewUser extends AnchorPane {

    /**
     * Initializes the controller class.
     */
    @FXML
    public TextField tfFirstName;
    @FXML
    public TextField tfLastName;
    @FXML
    public TextField tfEmail;
    @FXML
    public TextField tfPassword;
    @FXML
    public ComboBox country;
    @FXML
    public RadioButton rbMale;
    @FXML
    public RadioButton rbFemale;
    @FXML
    private HBox hbMenu;
    @FXML
    private Button bSignupUser;
    @FXML 
     ImageView logo;
    String output;
    UserOperations userOps;
    AdminMenu menu;
    Utilities util = new Utilities();
    User user;

    public AdminAddNewUser() {
        user = new User();
        userOps = new UserOperations();
        menu = new AdminMenu();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_add_new_user.fxml"));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                hbMenu.getChildren().add(menu);
            }
        });
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {

            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(AdminMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        ToggleGroup radioGroup = new ToggleGroup();
        rbMale.setToggleGroup(radioGroup);
        rbFemale.setToggleGroup(radioGroup);
        country.setStyle("-fx-background-radius: 30;");
        logo.setStyle("-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );");
        country.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                output = (String) country.getSelectionModel().getSelectedItem().toString();

            }

        });
        bSignupUser.setOnAction((ActionEvent event) -> {
            /*tfFirstName.getStyleClass().add("normal");
            tfLastName.getStyleClass().add("normal");
            tfEmail.getStyleClass().add("normal");
            tfPassword.getStyleClass().add("normal");
            tfCountry.getStyleClass().add("normal");*/

            if (!tfFirstName.getText().toString().equals("")) {
                user.setFirstName(tfFirstName.getText().toString());
            } else {
                tfFirstName.getStyleClass().add("error");
            }
            if (!tfLastName.getText().toString().equals("")) {
                user.setLastName(tfLastName.getText().toString());
            } else {
                tfLastName.getStyleClass().add("error");
            }
            if (!tfPassword.getText().toString().equals("")) {
                user.setPassword(tfPassword.getText().toString());
            } else {
                tfPassword.getStyleClass().add("error");
            }
            if (util.isValidEmail(tfEmail.getText().toString())) {
                user.seteMail(tfEmail.getText().toString());
            } else {
                tfEmail.getStyleClass().add("error");
            }
            if (rbMale.isSelected()) {
                user.setGender("male");
            }
            if (rbFemale.isSelected()) {
                user.setGender("female");
            }
            if (output != null) {
                user.setCountry(output);
            }
            else{
                country.getStyleClass().add("error");
            }
            if (((rbMale.isSelected()) || (rbFemale.isSelected()))
                    && !tfFirstName.getText().toString().equals("")
                    && !tfLastName.getText().toString().equals("")
                    && !tfPassword.getText().toString().equals("")
                    && util.isValidEmail(tfEmail.getText().toString())
                    && output != null) {

                userOps.addNewUser(user);
                TrayNotification successNotification = new TrayNotification("Success", "User has been added successfully", NotificationType.SUCCESS);
                successNotification.setAnimationType(AnimationType.POPUP);
                successNotification.showAndDismiss(Duration.millis(3000));
            } else {

                TrayNotification errorNotification = new TrayNotification("Error", "All Fields Must be filled and be valid", NotificationType.ERROR);
                errorNotification.setAnimationType(AnimationType.POPUP);
                errorNotification.showAndDismiss(Duration.millis(3000));
            }
        });
    }

}
