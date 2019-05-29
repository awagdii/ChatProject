/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import com.client.interfaces.impl.ClientImplementation;
import com.client.util.Utilities;
import com.server.interfaces.ServerInterface;
import com.server.interfaces.UserInterface;
import com.server.model.Server;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class ClientLogin extends AnchorPane {

    /**
     * Initializes the controller class.
     */
    //----------Server interfacesc----------------------------
    static ServerInterface serverInterface;
    static UserInterface userInterface;
    public static String currentEmail = " ";
    // String email;

    @FXML
    private Button bLogin;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Hyperlink hlSignup;
    @FXML
    private Hyperlink hlServerIP;
    @FXML
    private ImageView icon;
    @FXML
    private Popup popup;

    public ClientLogin() {
        try {

            //---------------------------- FXML Loading ---------------------------------------------------------
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/client_login.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
            ChatClient.primaryStage.setScene(new Scene(this));
            ChatClient.primaryStage.setResizable(false);
            ChatClient.primaryStage.setTitle("Welcome to iChat");
            ChatClient.primaryStage.show();
            icon.setFitHeight(100);
            icon.setFitWidth(200);
            icon.setImage(new Image("img/HipChatIcon.png"));
            File file = new File("config.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileReader fr = new FileReader(file)) {

                int content;
                String res = "";
                while ((content = fr.read()) != -1) {
                    res += (char) content;
                }
                Utilities.serverIP = res;

            } catch (IOException e) {
                e.printStackTrace();
            }

            //------------------------------ RMI Part -----------------------------------------------------------
            Registry registry = LocateRegistry.getRegistry(Utilities.serverIP, 9000);
            serverInterface = (ServerInterface) registry.lookup("ServerService");
            userInterface = (UserInterface) registry.lookup("UserService");
            Utilities.serverInterface = serverInterface;
            Utilities.userInterface = userInterface;

            //--------------------------- Handling Button Action -------------------------------------------------
            bLogin.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    //--------- Hide Popup if any ---------------------------
                    if (popup != null) {
                        popup.hide();
                    }
                    //--------------- Start Login process--------------------
                    String email = tfEmail.getText().toString();
                    String password = tfPassword.getText().toString();
                    if (!email.equals("") && !password.equals("")) {
                        if (Utilities.isValidEmail(email)) {
                            Server server = new Server(1, 1);
                            try {
                                Utilities.user.seteMail(email);
                                Utilities.myClientImp = new ClientImplementation();
                                Utilities.serverInterface.registerUser(Utilities.user.geteMail(), Utilities.myClientImp);
                                Utilities.user = Utilities.userInterface.getUserObject(email);
                                System.out.println("my mailllll " + Utilities.user.getFirstName());

                                boolean checkServer = serverInterface.checkServer(server);
                                if (checkServer) {
                                    boolean checUser = userInterface.loginUser(email, password);
                                    if (checUser) {
                                        TrayNotification successNotification = new TrayNotification("Success", "Welcome: " + email, NotificationType.SUCCESS);
                                        successNotification.setAnimationType(AnimationType.SLIDE);
                                        successNotification.showAndDismiss(Duration.millis(3000));
                                        currentEmail = email;
                                        ArrayList<String> contactListArrayList = new ArrayList<String>();
                                        ClientLogin.serverInterface.registerContactList(currentEmail);
                                        contactListArrayList = ClientLogin.serverInterface.tellOtherUserLogin(currentEmail);

                                        if (userInterface.getOfflineMesgs(Utilities.user.geteMail()).size() != 0) {
                                            new OfflineAnnouncements();
                                        }
                                        ChatClient.primaryStage.setScene(new Scene(new ContactList()));
                                    } else {

                                        Notifier.INSTANCE.notifyInfo("Notification", "User Name or Password is Wrong!");
                                    }
                                } else {

                                    Notifier.INSTANCE.notifyInfo("Notification", "Server is not Opened !");

                                }
                            } catch (RemoteException ex) {
                                Logger.getLogger(ClientLogin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            if (popup != null) {
                                popup.hide();
                            }
                            TrayNotification errorNotification = new TrayNotification("Error", "Invalid Email", NotificationType.ERROR);
                            errorNotification.setAnimationType(AnimationType.SLIDE);
                            errorNotification.showAndDismiss(Duration.millis(3000));
                        }
                    } else {
                        if (popup != null) {
                            popup.hide();
                        }
                        TrayNotification errorNotification = new TrayNotification("Error", "Email & Password must be filled", NotificationType.ERROR);
                        errorNotification.setAnimationType(AnimationType.SLIDE);
                        errorNotification.showAndDismiss(Duration.millis(3000));
                    }
                }
            });

            hlSignup.setOnAction(e -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/client/view/cfg/FXMLSignUp.fxml"));
                    // Parent root = new FXMLLoader.load(getClass().getResource("/com/client/view/cfg/FXMLSignUp.fxml"));
                    Scene scene = new Scene(root);

                    ChatClient.primaryStage.setScene(scene);
                    ChatClient.primaryStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                /*fxmlLoader.setRoot(this);
                 fxmlLoader.setController(this);

                 fxmlLoader.load();
                 ChatClient.primaryStage.setResizable(false);
                 ChatClient.primaryStage.setTitle("Welcome to iChat");*/

            });
            hlServerIP.setOnAction((e) -> {

                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Configuration");
                dialog.setHeaderText("Welcome ..");
                dialog.setContentText("Please Enter Server IP:");

                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                //  Utilities.serverIP = result.get();
                try {

                    if (result.isPresent()) {
                        Utilities.serverIP = result.get();
                        File file2 = new File("config.txt");
                        FileWriter fw = new FileWriter(file2);

                        // if file doesnt exists, then create it
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }
                        fw.write(result.get());
                        fw.flush();
                        fw.close();

                    }
                } catch (IOException ew) {
                    ew.printStackTrace();
                }
                System.out.println(Utilities.serverIP);
                new ClientLogin();
                //         ChatClient.main();

//                ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
                //               ChatClient.primaryStage.show();
            });

        } catch (RemoteException ex) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Configuration");
            dialog.setHeaderText("Your server IP is Wrong");
            dialog.setContentText("Please Enter A Valid Server IP:");
            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            try {

                if (result.isPresent()) {
                    Utilities.serverIP = result.get();

                    File file2 = new File("config.txt");
                    FileWriter fw = new FileWriter(file2);

                    // if file doesnt exists, then create it
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    fw.write(result.get());
                    fw.flush();
                    fw.close();

                }
                new ClientLogin();

            } catch (IOException ew) {
                ew.printStackTrace();
            }

            //          ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
            //          ChatClient.primaryStage.show();
            System.out.println(Utilities.serverIP);
        } catch (IOException ex) {
            Logger.getLogger(ClientLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ClientLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Button getbLogin() {
        return bLogin;
    }

    public void setbLogin(Button bLogin) {
        this.bLogin = bLogin;
    }

    public TextField getTfEmail() {
        return tfEmail;
    }

    public void setTfEmail(TextField tfEmail) {
        this.tfEmail = tfEmail;
    }

    public PasswordField getTfPassword() {
        return tfPassword;
    }

    public void setTfPassword(PasswordField tfPassword) {
        this.tfPassword = tfPassword;
    }

    public Hyperlink getHlSignup() {
        return hlSignup;
    }

    public void setHlSignup(Hyperlink hlSignup) {
        this.hlSignup = hlSignup;
    }

}
