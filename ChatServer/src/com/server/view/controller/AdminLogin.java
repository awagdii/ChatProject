/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.database.DBConnection;
import com.server.dao.AdminOperations;
import com.server.main.ChatServer;
import com.server.util.Utilities;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminLogin extends AnchorPane {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button bLogin;
    @FXML
    private Popup popup;
    Connection connection;
    AdminOperations adminOperations;

    public AdminLogin() {
        connection = DBConnection.getConnection();
        adminOperations = new AdminOperations();

        //----------------------Loading FXML File ------------------------------
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_login.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            //ChatServer.primaryStage.setResizable(false);
            ChatServer.primaryStage.setTitle("iChat Admin Login");
            ChatServer.primaryStage.setWidth(200);
            ChatServer.primaryStage.setHeight(670);
        } catch (IOException ex) {
            Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

        //----------------------Defining Login Button Action -------------------
        bLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //--------- Hide Popup if any ---------------------------

                if (popup != null) {
                    popup.hide();
                }
                //--------------- Start Login process--------------------
                String email = tfEmail.getText().toString();
                String password = tfPassword.getText().toString();
                if (!email.equals("") && !password.equals("")) {
                    if (Utilities.isValidEmail(email)) {

                        boolean checkAdmin = adminOperations.loginAdmin(email, password);
                        if (checkAdmin) {
                            TrayNotification successNotification = new TrayNotification("Welcome", "Welcome Back: "+email, NotificationType.SUCCESS);
                            successNotification.setAnimationType(AnimationType.SLIDE);
                            successNotification.showAndDismiss(Duration.millis(3000));
                            ChatServer.scene = new Scene(new AdminMainScreen());
                            ChatServer.primaryStage.setScene(ChatServer.scene);
                            ChatServer.primaryStage.setHeight(670);
                            ChatServer.primaryStage.setWidth(370.0);

                        } else {
                            System.out.println("Admin doesn't exist");
                            TrayNotification errorNotification = new TrayNotification("Error", "No Such Admin! , There is no admin with this username & Password", NotificationType.ERROR);
                            errorNotification.setAnimationType(AnimationType.POPUP);
                            errorNotification.showAndDismiss(Duration.millis(3000));
                        }

                    } else {
                        TrayNotification errorNotification = new TrayNotification("Error", "Invalid Email! , Please type a valid email", NotificationType.ERROR);
                        errorNotification.setAnimationType(AnimationType.POPUP);
                        errorNotification.showAndDismiss(Duration.millis(3000));
                    }
                } else {
                    TrayNotification errorNotification = new TrayNotification();
                    errorNotification.setNotificationType(NotificationType.ERROR);
                    errorNotification.setTitle("Error");
                    errorNotification.setMessage("Email & Password must be filled !");
                    errorNotification.setAnimationType(AnimationType.POPUP);
                    errorNotification.showAndDismiss(Duration.millis(3000));
                }
            }
        });

    }

    public TextField getTfEmail() {
        return tfEmail;
    }

    public void setTfEmail(TextField tfEmail) {
        this.tfEmail = tfEmail;
    }

    public TextField getTfPassword() {
        return tfPassword;
    }

    public void setTfPassword(PasswordField tfPassword) {
        this.tfPassword = tfPassword;
    }

    public Button getbLogin() {
        return bLogin;
    }

    public void setbLogin(Button bLogin) {
        this.bLogin = bLogin;
    }

}
