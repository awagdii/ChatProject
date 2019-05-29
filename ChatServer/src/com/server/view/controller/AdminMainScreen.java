/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.client.interfaces.ClientInterface;
import com.database.DBConnection;
import com.server.dao.ServerOperations;
import com.server.interfaces.impl.ServerImplementation;
import com.server.model.Server;
import com.server.toggle.ToggleSwitch;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminMainScreen extends AnchorPane implements ChangeListener {

    /**
     * Initializes the controller class.
     */
    @FXML
    private VBox vbToggleButtonContainer;
    @FXML
    private BorderPane hbMenu;
    @FXML
    private Label textServerStatus;
    ToggleSwitch toggleSwitch;
    ServerOperations serverOperations;
    Connection connection;

    public AdminMainScreen() {//---------------------------- FXML Loading ---------------------------------------------------------
        connection = DBConnection.getConnection();
        serverOperations = new ServerOperations();
        toggleSwitch = new ToggleSwitch();
        AdminMenu menu = new AdminMenu();
        initialServerState();
        //----------- calling the listener to handle turning the server on/off-----------------------------------------------------
        toggleSwitch.getSwitchOn().addListener(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_main_screen.fxml"));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbToggleButtonContainer.getChildren().add(toggleSwitch);
                
                hbMenu.setTop(menu);
                //hbMenu.getChildren().add(menu);
            }
        });

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        Server server = new Server();
        server.setServerId(1);
        if (toggleSwitch.getSwitchOn().getValue()) {
            System.out.println("On");

            serverOperations.openServer(connection, server);
            textServerStatus.setText("Server Is: ON");
        } else {
            serverOperations.closeServer(connection, server);
            
            
            try {
                ServerImplementation impl = new ServerImplementation();
                impl.initiateExitDialog("Server Closed");
            } catch (RemoteException ex) {
                Logger.getLogger(AdminMainScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            textServerStatus.setText("Server Is: OFF");
//            ClientInterface clientInterface ;
//            clientInterface.displayMessgaeInDialog("Server is closed");
            System.out.println("off");
        }
    }

    public void initialServerState() {
        Server server = new Server();
        server.setServerId(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (serverOperations.checkServer(connection, server)) {
                    textServerStatus.setText("Server is: ON");
                } else {
                    textServerStatus.setText("Server is: OFF");
                }
            }
        });

    }

    public void initialToggleState() {
        Server server = new Server();
        server.setServerId(1);
        if (serverOperations.checkServer(connection, server)) {
            toggleSwitch.setSwitchOn(true);
        } else {
            toggleSwitch.setSwitchOn(true);
        }
    }
}
