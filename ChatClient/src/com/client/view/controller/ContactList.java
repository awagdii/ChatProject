/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import com.client.util.Utilities;
import com.server.model.Server;
import com.server.model.User;
import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Nada
 */
public class ContactList extends AnchorPane {

    @FXML
    private ListView<User> contactListview;
    @FXML
    private ListView<User> requestListview;
    @FXML
    private Tab contactTab;
    @FXML
    private Tab requestTab;

    @FXML
    private Button addButton;
    @FXML
    private Button changeImage;
    @FXML
    private Label name;
    @FXML
    private Label gender;
    @FXML
    private TextField searchField;
    @FXML
    private ImageView myImage;
    @FXML
    private MenuItem logout;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem status;
    @FXML
    private MenuItem about;
    @FXML
    private TabPane tabPane;
    @FXML
    private Menu file;
    @FXML
    private Menu help;

    @FXML
    private Popup popup;
    @FXML
    private Button refreshButton;
    @FXML
    private ComboBox<Text> statusComboBox;
    @FXML
    private Circle statusShape;

    String currentEmail = ClientLogin.currentEmail;
    ArrayList<User> myFriends = new ArrayList<>();
    ArrayList<User> show = new ArrayList<User>();

    File selectedImage;
    byte[] bufferedImage;
    File fi;
    Timer timer = new Timer();
    ObservableList<User> contactList = FXCollections.observableArrayList();
    ObservableList<User> requestList = FXCollections.observableArrayList();
    Notification info;

    Text online = new Text("Online");
    Text offline = new Text("Offline");
    Text away = new Text("Away");
    Text busy = new Text("Busy");
    Tooltip example = new Tooltip();

    public ContactList() {
        User me = ClientLogin.userInterface.getUserObject(currentEmail);
        
        try {

            // contactList = FXCollections.observableArrayList();
            // requestList = FXCollections.observableArrayList();
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        myFriends = ClientLogin.userInterface.retrieveFriends(currentEmail);
                        // System.out.print("already retrieved the user");
                        for (int i = 0; i < myFriends.size(); i++) {
                            contactList.add(myFriends.get(i));
                        }
                        show = ClientLogin.userInterface.showRequest(currentEmail);

                        for (User user : show) {

                            User obj = new User();
                            obj = user;
                            requestList.add(obj);
                            System.out.println(requestList.size());
                        }

                    });
                }
            });
            th.setDaemon(true);
            th.start();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/test.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            // this.getStylesheets().add("src/com/style/client_login.css");
            name.setText(me.getFirstName());
            gender.setText(me.getGender());
            online.setFill(Color.WHITE);
            offline.setFill(Color.WHITE);
            away.setFill(Color.WHITE);
            busy.setFill(Color.WHITE);
            changeImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    try {
                        FileChooser pickedImage = new FileChooser();
                        pickedImage.setTitle("Choose your Image");
                        pickedImage.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.Gif"));
                        selectedImage = pickedImage.showOpenDialog(changeImage.getScene().getWindow());
                        if (selectedImage != null) {
                            fi = new File(selectedImage.getAbsolutePath());
                            bufferedImage = Files.readAllBytes(fi.toPath());
                            System.out.println(bufferedImage.toString());
                            ClientLogin.userInterface.saveImage(bufferedImage, currentEmail);
                            Image imgProfile = new Image("File:" + selectedImage.getAbsolutePath());
                            myImage.setImage(imgProfile);
                            updateFreindsUserImg(currentEmail);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            name.setTextAlignment(TextAlignment.LEFT);
            name.setFont(Font.font("verdana", FontWeight.BOLD, 12));
            gender.setFont(Font.font("verdana", FontWeight.BOLD, 12));
            name.setTextFill(Color.WHITE);
            gender.setTextFill(Color.WHITE);
            Effect myEffect = new DropShadow(BlurType.THREE_PASS_BOX, Color.WHITE, 10, 0, 0, 0);
            name.setEffect(myEffect);
            gender.setEffect(myEffect);

            about.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Stage myStage = new Stage();
                    AnchorPane aboutPane = new AnchorPane();
                    Label history = new Label("Chat Program was created by ........"
                            + "At February 2016");
                    history.setTextFill(Color.BLUE);
                    Button ok = new Button("OK");
                    ok.setRotate(USE_PREF_SIZE);
                    //aboutPane.s
                    aboutPane.getChildren().addAll(history, ok);
                    Scene aboutScene = new Scene(aboutPane);
                    //myStage.setMaxHeight(USE_PREF_SIZE);
                    //myStage.setMaxWidth(USE_PREF_SIZE);
                    myStage.setScene(aboutScene);
                    // myStage.setMaximized(false);
                    myStage.setResizable(false);
                    myStage.show();
                }
            });

            ByteArrayInputStream bais = new ByteArrayInputStream(me.getImage());
            Image imgProfile = new Image(bais);
            myImage.setFitHeight(200);
            myImage.setFitWidth(100);
            myImage.setImage(imgProfile);
            // contactListview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            searchField.setOnMouseMoved(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    example.setText("example@jets.com");
                    //  example.show(ChatClient.primaryStage);
                }
            });
            searchField.setTooltip(example);
            requestListview = (ListView<User>) this.lookup("#requestListview");
            tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                    if (newValue == contactTab) {
                        System.out.println("contact taaab");
                        contactListview.setItems(contactList);
                        contactListview.setCellFactory(new ListViewFactory());
                    } else {
                        requestListview.setItems(requestList);
                        requestListview.setCellFactory(new showRequestCellFactory());
                        System.out.println("show request: " + currentEmail);
                        System.out.println("Request taab");
                        //                       requestListview.setCellFactory(new ListViewFactory());
                    }
                }
            });

            contactListview.setItems(contactList);
            contactListview.setCellFactory(new ListViewFactory());
            requestListview.setItems(requestList);
            requestListview.setCellFactory(new showRequestCellFactory());
            ChatClient.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        ClientLogin.serverInterface.tellOtherUserLogout(currentEmail);
                        ClientLogin.serverInterface.unRegisterUser(Utilities.user.geteMail(), Utilities.myClientImp);
                        ClientLogin.userInterface.setUserToOffline(currentEmail);
                        // ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
                    } catch (RemoteException ex) {
                        Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

        } catch (IOException ex) {
            Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
        }
          refreshButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
         ChatClient.primaryStage.setScene(new Scene(new ContactList()));
         }
         });
        close.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    ClientLogin.serverInterface.registerContactList(currentEmail);
                    ClientLogin.serverInterface.tellOtherUserLogout(currentEmail);
                    ClientLogin.serverInterface.unRegisterUser(Utilities.user.geteMail(), Utilities.myClientImp);
                    ClientLogin.userInterface.setUserToOffline(currentEmail);
                    ChatClient.primaryStage.close();
                    System.out.println("logout ");
                } catch (RemoteException ex) {
                    Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ClientLogin.serverInterface.registerContactList(currentEmail);
                    ClientLogin.serverInterface.tellOtherUserLogout(currentEmail);
                    ClientLogin.serverInterface.unRegisterUser(Utilities.user.geteMail(), Utilities.myClientImp);
                    //      ChatClient.primaryStage.setScene(new Scene(new ClientLogin()));
                    ClientLogin.userInterface.setUserToOffline(currentEmail);
                    ChatClient.primaryStage.close();
                    System.out.println("logout ");
                } catch (RemoteException ex) {
                    Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //--------------- Start sending request process--------------------
                String search = searchField.getText().toString();
                if (!search.equals("")) {
                    if (Utilities.isValidEmail(search)) {
                        Server server = new Server(1, 1);
                        try {
                            boolean checkServer = ClientLogin.serverInterface.checkServer(server);
                            if (checkServer) {
                                searchRequestedEmail(search);
                            } else {
                                System.out.println("Server is not opened");
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(ClientLogin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {

                        Label label = new Label("Invalid Email! !");
                        label.setTextFill(Color.WHITE);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Email");
                        alert.setContentText("Invalid Email , Make sure of Email !");
                        alert.showAndWait();
                    }
                } else {

                    Label label = new Label("Enter Email to search for it!");
                    label.setTextFill(Color.WHITE);
                    /* popup = new Popup();
                     popup.getContent().add(label);
                     popup.show(ChatClient.primaryStage);*/
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Enter Email to search for it !");
                    alert.showAndWait();
                }
            }
        });
        statusComboBox.getItems().addAll(online, busy, away, offline);
        statusComboBox.getSelectionModel().select(new Text(getStatus(me.getStatus())));
        statusComboBox.setCellFactory(new statusCellFactory());
        statusComboBox.setButtonCell(statusComboBox.getCellFactory().call(null));
        System.out.println("current status" + statusComboBox.getSelectionModel().getSelectedItem().getText());
        statusComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeStatus(getStatusInt(statusComboBox.getSelectionModel().getSelectedItem().getText()), currentEmail);
                updateFreindsUserStatus(getStatusInt(statusComboBox.getSelectionModel().getSelectedItem().getText()), currentEmail);
            }
        });
    }

    public boolean searchRequestedEmail(String Email) {
        boolean flag = ClientLogin.userInterface.searchRequestedEmail(Email);
        System.out.println(flag);
        if (flag) {
            boolean contactlistflag = ClientLogin.userInterface.searchContactList(currentEmail, Email);
            if (contactlistflag) {
                User usr = new User();
                usr = ClientLogin.userInterface.getUserObject(Email);
                boolean sendrequest = ClientLogin.userInterface.sendRequest(currentEmail, Email);
                try {
                    ClientLogin.serverInterface.tellOtherSendingRequest(currentEmail, Email);
                } catch (RemoteException ex) {
                    Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(sendrequest);
            } else {
                // System.out.println("Already friend");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Already Friend");
                alert.setContentText("you are already Friends");
                alert.showAndWait();
            }
        } else {
            // System.out.println("no user with this email");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid User");
            alert.setContentText("There is no user with this mail");
            alert.showAndWait();
        }
        return flag;
    }

    public void updateFreindsUserStatus(int status, String Email) {
        ArrayList<String> contactListArrayList = new ArrayList<String>();
        try {
            ClientLogin.serverInterface.registerContactList(currentEmail);
            contactListArrayList = ClientLogin.serverInterface.tellOtherStatusModeChanged(getStatusInt(statusComboBox.getSelectionModel().getSelectedItem().getText()), currentEmail);
        } catch (RemoteException ex) {
            Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFreindsUserImg(String Email) {
        ArrayList<String> contactListArrayList = new ArrayList<String>();
        try {
            ClientLogin.serverInterface.registerContactList(currentEmail);
            contactListArrayList = ClientLogin.serverInterface.tellOtherUserImgChanged(currentEmail);
        } catch (RemoteException ex) {
            Logger.getLogger(ContactList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void notifyStatustoOther(String email, int status) {
        Platform.runLater(() -> {
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));
            Notifier.INSTANCE.notifyInfo("Notification", email + "is" + getStatus(status));
        });
    }

    public void notifyUserImgChanged(String email) {
        Platform.runLater(() -> {
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));
        });
    }

    public void notifyUserLogintoOther(String email) {
        Platform.runLater(() -> {
            Notifier.INSTANCE.notifyInfo("Notification", email + "is online");
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));

        });
    }

    public void notifyAcceptingRequest(String email) {
        Platform.runLater(() -> {
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));
            Notification.Notifier.INSTANCE.notifyInfo("Notification", email + " Accepted your request");
        });
    }

    public void notifySendingRequest(String email) {
        Platform.runLater(() -> {
            Notification.Notifier.INSTANCE.notifyInfo("Notification", email + " wants to be your friends");
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));
        });
    }

    public void notifyRejectingRequest(String email) {
        Platform.runLater(() -> {
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));
        });
    }

    public void notifyUserLogouttoOther(String email) {
        Platform.runLater(() -> {
            Notifier.INSTANCE.notifyInfo("Notification", email + " logout");
            ChatClient.primaryStage.setScene(new Scene(new ContactList()));

        });
    }

    public boolean changeStatus(int status, String Email) {
        boolean flag = ClientLogin.userInterface.changeStatus(status, Email);
        System.out.println(flag);
        if (flag) {
            System.out.println("status changed");
        } else {
            System.out.println("status not changed");

        }
        return flag;
    }

    public String getStatus(int status) {
        String myStatus = null;
        if (status == 1) {
            myStatus = "Online";
            //  statusShape.setFill(Color.GREEN);

        } else if (status == 2) {
            myStatus = "Busy";
            // statusShape.setFill(Color.RED);
        } else if (status == 3) {
            myStatus = "Away";
            // statusShape.setFill(Color.ORANGE);
        } else if (status == 4) {
            myStatus = "Offline";
            //statusShape.setFill(Color.WHITE);
        }
        return myStatus;
    }

    public int getStatusInt(String status) {
        int myStatus = 0;
        System.out.println("get status int function " + status);
        if (status.equals("Online")) {
            myStatus = 1;
            // statusShape.setFill(Color.GREEN);
        } else if (status.equals("Busy")) {
            myStatus = 2;
            // statusShape.setFill(Color.RED);
        } else if (status.equals("Away")) {
            myStatus = 3;
            // statusShape.setFill(Color.ORANGE);
        } else if (status.equals("Offline")) {
            myStatus = 4;
            // statusShape.setFill(Color.GRAY);
        }
        return myStatus;
    }

}
