/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.server.main.ChatServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminMenu extends AnchorPane implements EventHandler<ActionEvent> {

    @FXML
    MenuItem miAddNewAdmin, miAddNewUsers, miLogout, miExit, miViewStatisticsReports, miAbout, announcemi;

    public AdminMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_menu.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(AdminMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        miAddNewAdmin.setOnAction(this);
        miAddNewUsers.setOnAction(this);
        miLogout.setOnAction(this);
        miExit.setOnAction(this);
        miViewStatisticsReports.setOnAction(this);
        miAbout.setOnAction(this);
        announcemi.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == miAddNewAdmin) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ChatServer.primaryStage.setScene(new Scene(new AdminAddNewAdmin()));
                    ChatServer.primaryStage.setTitle("Add New Admin Screen");
                    ChatServer.primaryStage.setHeight(670);
                    ChatServer.primaryStage.setWidth(390);
                }
            });
        } else if (event.getSource() == miAddNewUsers) {
            ChatServer.primaryStage.setScene(new Scene(new AdminAddNewUser()));
            ChatServer.primaryStage.setTitle("Add New User");
            ChatServer.primaryStage.setHeight(670);
            ChatServer.primaryStage.setWidth(370);
        } else if (event.getSource() == miLogout) {
            ChatServer.primaryStage.setScene(new Scene(new AdminLogin()));
            ChatServer.primaryStage.setTitle("iChat Admin Login");
            ChatServer.primaryStage.setHeight(670);
            ChatServer.primaryStage.setWidth(390);
        } else if (event.getSource() == miExit) {
            Platform.exit();
            System.exit(0);
        } else if (event.getSource() == miViewStatisticsReports) {
            System.out.println("admin statistics");
            ChatServer.primaryStage.setScene(new Scene(new AdminStatistics()));
            ChatServer.primaryStage.setTitle("Admin Statistics Screen");
            ChatServer.primaryStage.setHeight(670);
            ChatServer.primaryStage.setWidth(390);

        } else if (event.getSource() == miAbout) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About This Software");
            alert.setHeaderText("About This Software");
            alert.setContentText("This App is developed by : Java Team");
            alert.showAndWait();

        } else if (event.getSource() == announcemi) {
            new SendAnnounce();

        }

    }

    /**
     * Initializes the controller class.
     */
}
