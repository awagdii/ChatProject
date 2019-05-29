/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import com.client.interfaces.ClientInterface;
import com.client.util.Utilities;
import com.server.model.User;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Nada
 */
public class OnlineFriends extends AnchorPane {

    @FXML
    ListView onlineFriends;

    String currentEmail = ClientLogin.currentEmail;
    ObservableList<User> onlineFriend = FXCollections.observableArrayList();
    ArrayList<User> myFriends = new ArrayList<>();
    ArrayList<User> group = new ArrayList<>();

    public OnlineFriends(ChatWindow currentChatWindow) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/group_chat.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        try {
                            Vector<String> onlineEmail = new Vector<String>();
                            for (int i = 0; i < currentChatWindow.friendObj.size(); i++) {
                                System.out.println(currentChatWindow.friendObj.get(i).getMail());
                                onlineEmail.add(currentChatWindow.friendObj.get(i).getMail());
                            }

                            myFriends = ClientLogin.userInterface.getOnlineFriends(currentEmail);
                            for (int i = 0; i < myFriends.size(); i++) {
                                ClientInterface receiverObj = Utilities.serverInterface.askForConn(myFriends.get(i).geteMail());
                                if (receiverObj != null && receiverObj != Utilities.myClientImp&&!onlineEmail.contains(myFriends.get(i).geteMail())) {
                                    onlineFriend.add(myFriends.get(i));
                                }
                            }

//                            for (int i = 0; i < currentChatWindow.friendObj.size(); i++) {
//
//                                onlineFriend.remove(currentChatWindow.friendObj.get(i).getUser());
//                            }

                        } catch (RemoteException ex) {
                            Logger.getLogger(OnlineFriends.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    });
                }
            });
            th.setDaemon(true);
            th.start();

            onlineFriends.setItems(onlineFriend);
            onlineFriends.setCellFactory(new OnlineFactory(currentChatWindow));

        } catch (IOException ex) {
            Logger.getLogger(OnlineFriends.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
