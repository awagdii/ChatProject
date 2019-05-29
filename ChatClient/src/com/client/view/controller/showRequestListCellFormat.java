/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import com.server.interfaces.UserInterface;
import com.server.model.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class showRequestListCellFormat extends ListCell<User> {

    UserInterface userInterface;
    static String currentEmail = ClientLogin.currentEmail;

    public showRequestListCellFormat() {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(9000);
            userInterface = (UserInterface) registry.lookup("UserService");

        } catch (RemoteException ex) {
            Logger.getLogger(ShowRequestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ShowRequestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ShowRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void updateItem(User item, boolean empty) {

        try {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setGraphic(null);
            } else {
                FXMLLoader loader = new FXMLLoader();

                Parent root = loader.load(getClass().getResource("/com/client/view/cfg/show_request.fxml").openStream());
                ImageView imageView = (ImageView) root.lookup("#imageViewPicture");
                Label labelName = (Label) root.lookup("#labelName");
                Button acceptButton = (Button) root.lookup("#acceptButton");
                Button rejectButton = (Button) root.lookup("#rejectButton");
                labelName.setText(item.getFirstName());
                ByteArrayInputStream bais = new ByteArrayInputStream(item.getImage());
                Image imgProfile = new Image(bais);
                //      myImage.setImage(imgProfile);

                imageView.setImage(imgProfile);
                acceptButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        System.out.println("accept Button");
                        ChatClient.primaryStage.setScene(new Scene(new ContactList()));
                        acceptRequest(item.geteMail());
                        try {
                            ClientLogin.serverInterface.tellOtherRequestAccepted(currentEmail, item.geteMail());
                        } catch (RemoteException ex) {
                            Logger.getLogger(showRequestListCellFormat.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                rejectButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        ChatClient.primaryStage.setScene(new Scene(new ContactList()));
                        System.out.println("reject Button");
                        rejectRequest(item.geteMail());
                        try {
                            ClientLogin.serverInterface.tellOtherRequestRejected(currentEmail, item.geteMail());
                        } catch (RemoteException ex) {
                            Logger.getLogger(showRequestListCellFormat.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });
                setGraphic(root);
            }

        } catch (IOException ex) {
            Logger.getLogger(showRequestListCellFormat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean acceptRequest(String freindEmail) {
        return userInterface.AcceptRequest(currentEmail, freindEmail);
    }

    public boolean rejectRequest(String freindEmail) {
        return userInterface.rejectRequest(currentEmail, freindEmail);
    }

}
