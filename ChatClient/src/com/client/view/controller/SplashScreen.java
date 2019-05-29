/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class SplashScreen extends AnchorPane {

    /**
     * Initializes the controller class.
     */
    public SplashScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/splash_screen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
