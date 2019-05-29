/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import com.client.util.Utilities;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class OfflineAnnouncements extends AnchorPane {

    @FXML
    TextArea myTextArea;

    public OfflineAnnouncements() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/Offline_Announcements.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            Platform.runLater(() -> {

                Stage myStage = new Stage();
                myStage.setScene(new Scene(this));
                myStage.show();

                try {
                    ArrayList<String> myOffmsgs = new ArrayList<String>();
                    myOffmsgs = Utilities.userInterface.getOfflineMesgs(Utilities.user.geteMail());
                    for (int i = 0; i < myOffmsgs.size(); i++) {
                        myTextArea.appendText(myOffmsgs.get(i) + "-------------------------------------------------------\n");
                    }
                    
                    Utilities.userInterface.removeOfflineMesgs(Utilities.user.geteMail());

                } catch (RemoteException ex) {
                    Logger.getLogger(OfflineAnnouncements.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

        } catch (IOException ex) {
            Logger.getLogger(OfflineAnnouncements.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
