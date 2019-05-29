/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import com.client.interfaces.ClientInterface;
import com.client.interfaces.impl.ClientImplementation;
import static com.client.interfaces.impl.ClientImplementation.chatWindows;
import com.client.util.Utilities;
import com.server.model.User;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 *
 * @author Nada
 */
public class OnlineCellFactory extends ListCell<User> {

    static Vector<ClientInterface> friendObj = new Vector<ClientInterface>();
    ChatWindow currentChatWindow;

    public OnlineCellFactory(ChatWindow currentChatWindow) {
        this.currentChatWindow = currentChatWindow;
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
        } else {
            HBox mybox = new HBox();
            mybox.setAlignment(Pos.CENTER_LEFT);
            mybox.setSpacing(12);
            Label onlineUser = new Label();
            Button addButton = new Button("Add");
            onlineUser.setText(item.getFirstName());
            addButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    Platform.runLater(() -> {
                        try {

                            Iterator<ChatWindow> chatWindowsIterator = chatWindows.values().iterator();
                            while (chatWindowsIterator.hasNext()) {
                                ChatWindow currWindow = chatWindowsIterator.next();
                                if (currWindow.equals(currentChatWindow)) {
                                    chatWindowsIterator.remove();
                                    System.out.print("removed");
                                }
                            }

                            currentChatWindow.friendObj.add(Utilities.serverInterface.askForConn(item.geteMail()));
                            ClientImplementation.chatWindows.put(currentChatWindow.friendObj, currentChatWindow);
                            currentChatWindow.online.close();
                        } catch (RemoteException ex) {
                            Logger.getLogger(OnlineCellFactory.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    });
//
//                        friendObj.add(Utilities.serverInterface.askForConn(Utilities.user.geteMail()));
//                        friendObj.add(Utilities.serverInterface.askForConn(item.geteMail()));
                    //System.out.println("User add to conv " + item.geteMail());
                    for (ClientInterface email : friendObj) {
                        System.out.println(" conv " + email);
                    }
                    //Utilities.serverInterface.askForConn(Utilities.user.geteMail()).startChatWindow(friendObj);

                }
            });
            mybox.getChildren().addAll(onlineUser, addButton);
            setGraphic(mybox);
        }

    }

}
