/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.server.dao.UserOperations;
import com.server.main.ChatServer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminStatistics extends AnchorPane implements EventHandler<ActionEvent> {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label tStatisticsOnlineUsersNumber, tStatisticsFemaleUsersNumber, tStatisticsMaleUsersNumber, tStatisticsTotalUsersNumber;
    @FXML
    private Button bStatisticsBack;
    @FXML
    private Hyperlink hlStatisticsAdvanced;
    @FXML
    private HBox hbMenu, hbMenu2;

    UserOperations userOps;
    AdminMenu menu;

    public AdminStatistics() {
        userOps = new UserOperations();
        menu = new AdminMenu();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_statistics.fxml"));
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
        bStatisticsBack.setOnAction(this);
        hlStatisticsAdvanced.setOnAction(this);

        //--------------------Set initial Statistic when AdminStatistics Load-----------------------
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setTStatisticsOnlineUsersNumber();
                setTStatisticsFemaleUsersNumber();
                setTStatisticsMaleUsersNumber();
                setTStatisticsTotalUsersNumber();
            }
        }, 0, 10000);

        //------------------------------------------------------------------------------------------
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == bStatisticsBack) {
            ChatServer.primaryStage.setScene(new Scene(new AdminMainScreen()));
            ChatServer.primaryStage.setTitle("Admin Main Screen");
            ChatServer.primaryStage.setHeight(670);
        } else if (event.getSource() == hlStatisticsAdvanced) {
            ChatServer.primaryStage.setScene(new Scene(new AdminStatisticsAdvanced()));
            ChatServer.primaryStage.setTitle("Admin Advanced Statistics Screen");
            ChatServer.primaryStage.setHeight(670);
            ChatServer.primaryStage.setWidth(750);
        }
    }

    public void setTStatisticsOnlineUsersNumber() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tStatisticsOnlineUsersNumber.setText(userOps.getNumberOfOnlineUsers() + "");
            }
        });

    }

    public void setTStatisticsFemaleUsersNumber() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tStatisticsFemaleUsersNumber.setText(userOps.getNumberOfFemaleUsers() + "");
            }
        });

    }

    public void setTStatisticsMaleUsersNumber() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tStatisticsMaleUsersNumber.setText(userOps.getNumberOfMaleUsers() + "");
            }
        });

    }

    public void setTStatisticsTotalUsersNumber() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tStatisticsTotalUsersNumber.setText(userOps.getTotalNumberOfUsers() + "");
            }
        });

    }

}
