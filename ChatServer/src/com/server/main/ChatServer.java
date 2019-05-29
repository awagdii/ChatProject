/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.main;

import com.database.DBConnection;
import com.server.interfaces.impl.HelloImp;
import com.server.interfaces.impl.ServerImplementation;
import com.server.interfaces.impl.UserImplementation;
import com.server.view.controller.AdminLogin;
import insidefx.undecorator.Undecorator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Shall
 */
public class ChatServer extends Application {

    public static Stage primaryStage;
    public static Scene scene;
    public static Undecorator undecorator;

    public static void main(String[] args) {

        /* Connection con = DBConnection.getConnection();
         try {

         FileInputStream im = new FileInputStream("D:\\Mohamed\\fcih\\Master\\ITI\\Diploma\\Java\\Projects\\Chat Project\\versions\\ChatV1\\ChatV1\\pic.jpg");
         String query = "update userinfo set userImage=? ";
         java.sql.PreparedStatement stmt = con.prepareStatement(query);
         stmt.setBinaryStream(1, im, im.available());
         stmt.execute();
  
         } catch (SQLException ex) {
         Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (FileNotFoundException ex) {
         Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //------------------------ Handling Registry-------------------------------
        try {
            Registry registry = LocateRegistry.createRegistry(9000);
            UserImplementation userImplementation = new UserImplementation();
            ServerImplementation serverImplementation = new ServerImplementation();
            HelloImp hello = new HelloImp();
            registry.rebind("HelloService", hello);
            registry.rebind("UserService", userImplementation);
            registry.rebind("ServerService", serverImplementation);
        } catch (RemoteException ex) {
            TrayNotification errorNotification = new TrayNotification("Error", "Your registery is busy please close other server that run on that port", NotificationType.ERROR);
                        errorNotification.setAnimationType(AnimationType.SLIDE);
                        errorNotification.showAndDismiss(Duration.millis(3000));
            
        }
        //-------------------------Handling FX ---------------------------------
        this.primaryStage = primaryStage;
        AdminLogin adminLogin = new AdminLogin();
        
        undecorator = new Undecorator(ChatServer.primaryStage, adminLogin);
        undecorator.getStylesheets().add("skin/undecorator.css");
        scene = new Scene(undecorator);
        scene.setFill(Color.TRANSPARENT);
        ChatServer.primaryStage.initStyle(StageStyle.TRANSPARENT);
        this.primaryStage.setScene(scene);
        this.primaryStage.setWidth(390.0);
        this.primaryStage.setHeight(670.0);
        this.primaryStage.show();
        primaryStage.setOnCloseRequest((e)->
        {
            System.exit(0);
        });
    }

}
