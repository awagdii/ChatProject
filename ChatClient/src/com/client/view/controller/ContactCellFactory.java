/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import com.client.interfaces.ClientInterface;
import com.client.util.Utilities;
import com.server.model.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import com.client.view.controller.ContactList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Vector;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.omg.CORBA.portable.InputStream;

/**
 *
 * @author Nada
 */
class ContactCellFactory extends ListCell<User> {

    ContactList contactList;
    Circle statusShape;
    Circle myImage = new Circle(28, 48, 25);
    Label name = new Label();

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
        } else {
            HBox mybox = new HBox();
            mybox.setAlignment(Pos.CENTER_LEFT);
            Tooltip email = new Tooltip();
            mybox.setPadding(new Insets(0, 10, 10, 5));
            mybox.setSpacing(60);

            name.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 12));
            name.setStyle(null);
            ImageView userImage = new ImageView();
            name.setText(item.getFirstName());
            ByteArrayInputStream imageStream = new ByteArrayInputStream(item.getImage());
            Image image = new Image(imageStream);
            myImage.setFill(new ImagePattern(image));
            myImage.setStrokeWidth(3);
            statusShape = new Circle(7);
            System.out.println(item.getRealStatus());
            if (item.getRealStatus() == 1) {
                getStatus(item.getStatus());
            } else {
                //  statusShape.setFill(Color.GREY);
                name.setTextFill(Color.BLACK);
                myImage.setStroke(Color.GREY);
            }
            name.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (item.getRealStatus() == 1) {
                        if (item.getStatus() == 4) {
                            email.setText("Offline");
                        } else if (item.getStatus() == 1) {
                            email.setText("Online");
                        } else if (item.getStatus() == 2) {
                            email.setText("Busy");
                        } else if (item.getStatus() == 3) {
                            email.setText("Away");
                        }
                    } else {
                        email.setText("Offline");
                    }
                }
            });
            myImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        System.out.println(Utilities.user.geteMail());
                        Vector<ClientInterface> friendObj = new Vector<ClientInterface>();

                        if (Utilities.serverInterface.askForConn(item.geteMail()) != null) {

                            friendObj.add(Utilities.serverInterface.askForConn(Utilities.user.geteMail()));
                            friendObj.add(Utilities.serverInterface.askForConn(item.geteMail()));
                            if (!Utilities.serverInterface.askForConn(Utilities.user.geteMail()).haveChatWindow(friendObj)) {
                                Utilities.serverInterface.askForConn(Utilities.user.geteMail()).startChatWindow(friendObj);
                            }
                        } else {
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Sent Offline Message");
                            dialog.setHeaderText(item.getFirstName() + " is Offline");
                            dialog.setContentText("Please Enter Message:");

                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                Utilities.userInterface.sendOfflineMsg(Utilities.user.geteMail(), item.geteMail(), result.get().toString());
                            }

                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(ContactCellFactory.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            /* myImage.setOnMouseClicked(new EventHandler<MouseEvent>(){

             @Override
             public void handle(MouseEvent event) {
                    
             }
                
             });*/
            name.setTooltip(email);
            mybox.getChildren().addAll(myImage, name);
            setGraphic(mybox);

        }

    }

    public String getStatus(int status) {
        String myStatus = null;
        if (status == 1) {
            // statusShape.setFill(Color.GREEN);
            myImage.setStroke(Color.GREEN);
            name.setTextFill(Color.GREEN);

        } else if (status == 2) {
            // statusShape.setFill(Color.RED);
            myImage.setStroke(Color.RED);
            name.setTextFill(Color.RED);
        } else if (status == 3) {
            //statusShape.setFill(Color.ORANGE);
            name.setTextFill(Color.ORANGE);
            myImage.setStroke(Color.ORANGE);
        } else if (status == 4) {
            // statusShape.setFill(Color.WHITE);
            name.setTextFill(Color.GREY);
            myImage.setStroke(Color.WHITE);
        }
        return myStatus;
    }

}
