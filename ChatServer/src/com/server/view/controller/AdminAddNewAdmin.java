/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.server.dao.AdminOperations;
import com.server.main.ChatServer;
import com.server.model.Admin;
import com.server.util.Utilities;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminAddNewAdmin extends AnchorPane implements EventHandler<ActionEvent> {

    @FXML
    private Button bAdminAdd, bAdminCancel;
    @FXML
    private TextField tfAdminName, tfAdminEmail;
    @FXML
    private PasswordField tfAdminPassword;
    @FXML
    private HBox hbMenu;
    @FXML
    private Text tErrorMessage;
    AdminOperations adminOps;

    public AdminAddNewAdmin() {
        adminOps = new AdminOperations();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_add_new_admin.fxml"));
        //------------------ Define the menu including its items ------------------------------------------
        AdminMenu menu = new AdminMenu();
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
        bAdminAdd.setOnAction(this);
        bAdminCancel.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == bAdminAdd) {
            adminRegister();
        } else if (event.getSource() == bAdminCancel) {
            ChatServer.primaryStage.setScene(new Scene(new AdminMainScreen()));
        }
    }

    /**
     * Initializes the controller class.
     */
    public void adminRegister() {
        String adminName = tfAdminName.getText().toString();
        String adminEmail = tfAdminEmail.getText().toString();
        String adminPassword = tfAdminPassword.getText().toString();
        System.out.println(adminName + " : " + adminEmail + " : " + adminPassword);
        Admin admin = new Admin(adminName, adminEmail, adminPassword);
        if (!adminName.equals("") && !adminEmail.equals("") && !adminPassword.equals("")) {
            if (Utilities.isValidEmail(adminEmail)) {
                boolean adminExists = adminOps.checIfAdminExists(adminEmail, adminPassword);
                if (adminExists) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setContentText("Error: There is an admin with this name ");
                    alert.showAndWait();
                } else {
                    boolean addResult = adminOps.addNewAdmin(admin);
                    if (addResult) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success!");
                        alert.setContentText("New admin " + adminEmail + " is added now");
                        alert.showAndWait();
                        ChatServer.primaryStage.setScene(new Scene(new AdminMainScreen()));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setContentText("Couldn't add new admin ");
                        alert.showAndWait();
                    }
                }
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tErrorMessage.setText("Valid email required !");
                    }
                });

            }
        }
    }
}
