/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Shall
 */
public class DialogHandler extends AnchorPane implements EventHandler<ActionEvent> {

    @FXML
    Button bDialogOk;
    @FXML
    Text tDialogMessage;
    static Stage dialogStage;

    static {
        dialogStage = new Stage();
    }

    //------------------- Method that initiated Dialog when server gets closed------------------------------------
    public DialogHandler() {
        try {
            //---------------------------- FXML Loading ---------------------------------------------------------
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/dialog_handler.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);

            fxmlLoader.load();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tDialogMessage.setText("Server is Down now !");
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Error");
                    dialogStage.setOnCloseRequest((WindowEvent t) -> {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                dialogStage.hide();
                                //ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
                                new ClientLogin();
                            }
                        });
                    });
                }
            });

            //ivLogo.setImage(new Image("img/logo.png"));
            //--------------------------- Handling Button Action -------------------------------------------------
            bDialogOk.setOnAction(this);
        } catch (IOException ex) {
            Logger.getLogger(DialogHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void initiateExitDialog() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setScene(new Scene(new DialogHandler()));
                dialogStage.show();

            }
        });

    }

    @Override
    public void handle(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dialogStage.hide();
               // ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
                new ClientLogin();
            }
        });
    }
}
