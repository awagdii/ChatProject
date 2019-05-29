/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import com.client.view.controller.ClientLogin;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Shall
 */
public class ChatClient extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

//        SendRequest sendRequest = new SendRequest();
//        this.primaryStage.setScene(new Scene(clientLogin));
        new ClientLogin();
        this.primaryStage.setWidth(370.0);
        this.primaryStage.setHeight(649.0);
        // this.primaryStage.show();
        primaryStage.setOnCloseRequest((e) -> {
            System.exit(0);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
