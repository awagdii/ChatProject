/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import static chatclient.ChatClient.primaryStage;
 import com.server.interfaces.ServerInterface;
import com.server.interfaces.UserInterface;
import com.server.model.User;
import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author aya
 */
public class ShowRequestController extends GridPane
{

    /**
     * Initializes the controller class.
     */
    UserInterface userInterface;
    static String currentEmail = ClientLogin.currentEmail;

    @FXML
    private ImageView imageViewPicture;
    @FXML
    private Label labelName;

    public ShowRequestController() {

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(9000);
            userInterface = (UserInterface) registry.lookup("UserService");
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/show_request.fxml"));
//            fxmlLoader.setRoot(this);
//            fxmlLoader.setController(this);
//            fxmlLoader.load();
        } catch (RemoteException ex) {
            Logger.getLogger(ShowRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (NotBoundException ex) {
            Logger.getLogger(ShowRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            showRequest();
        } catch (IOException ex) {
            Logger.getLogger(ShowRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showRequest() throws IOException {
        //   Parent root = FXMLLoader.load(getClass().getResource("/com/client/view/cfg/show_request.fxml"));

        ListView<User> list = new ListView<User>();

        list.setCellFactory(new showRequestCellFactory());
        System.out.println("show request: " + currentEmail);

        ArrayList<User> show = userInterface.showRequest(currentEmail);
        ObservableList<User> items= FXCollections.observableArrayList();
        for (User user : show) {
            //    list.getItems().add(new User(user.getFirstName(), user.geteMail(), user.getGender(), user.getCountry()));
            User obj= new User();
            obj= user;
            list.getItems().add(obj);
            System.out.println("" + user.getFirstName());
            System.out.println("" + user.geteMail());
            items.add(obj);
            System.out.println(items.size());
        }
          list.setItems(items);
          this.getChildren().add(list);
       list.setPrefWidth(340);

//        Scene scene = new Scene(list);
 //       primaryStage.setScene(scene);
 //       primaryStage.show();
    }
      

}
