/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.interfaces.impl;

import chatclient.ChatClient;
import com.client.interfaces.ClientInterface;
import com.client.util.Utilities;
import com.client.view.controller.ChatWindow;
import com.client.view.controller.ContactList;
import com.client.view.controller.DialogHandler;
import com.client.view.controller.MainScreen;
import com.client.view.controller.ProgBar;
import com.server.model.User;
import eu.hansolo.enzo.notification.Notification;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.System.in;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Shall
 */
public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {

    DialogHandler dialogHandler;
    public static HashMap<Vector< ClientInterface>, ChatWindow> chatWindows = new HashMap<Vector< ClientInterface>, ChatWindow>();
    HashMap<File, ProgBar> progBars = new HashMap<File, ProgBar>();

    public ClientImplementation() throws RemoteException {
        dialogHandler = new DialogHandler();
    }

    @Override
    public void displayMessgaeInDialog(String message) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dialogHandler.initiateExitDialog();
            }
        });
    }

    @Override
    public boolean sendFile(String filename, byte[] data, int len, String senderName) throws RemoteException {
        Platform.runLater(() -> {
            try {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("File recived from : " + senderName);
                fileChooser.setInitialFileName(filename);
                System.out.println(len);
                File file = fileChooser.showSaveDialog(ChatClient.primaryStage);

                if (file != null) {
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file, true);
                    out.write(data, 0, len);
                    out.flush();
                    out.close();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return true;
    }

    @Override
    public boolean startChatWindow(Vector<ClientInterface> senderObj) throws RemoteException {
        ChatWindow myWindow = new ChatWindow(senderObj);
        chatWindows.put(senderObj, myWindow);
        return true;
    }

    @Override
    public boolean sendMsg(Vector<ClientInterface> senderObj, String msg) throws RemoteException {

        Iterator<Vector<ClientInterface>> keySetIterator = chatWindows.keySet().iterator();

        System.out.println(chatWindows.size());
        while (keySetIterator.hasNext()) {
            Vector<ClientInterface> key = keySetIterator.next();
            for (int i = 0; i < key.size(); i++) {
                if ((key.get(i).getMail()).equals(senderObj.get(i).getMail())) {
                    System.out.println(msg);

                    chatWindows.get(key).displayMsg(msg, senderObj);
                    break;
                }

            }

        }

        return true;

    }

    @Override
    public String getMail() throws RemoteException {
        return Utilities.user.geteMail();
    }

    @Override
    public void receiveUserLogin(String email) throws RemoteException {
        System.out.println("receiving USER login method at user implementation  email is " + email);

        ContactList contactList = new ContactList();

        contactList.notifyUserLogintoOther(email);

    }

    @Override
    public void receiveUserImgChanged(String email) throws RemoteException {
        System.out.println("receiving method at user implementation IMG CHANGED for email  " + email);
        ContactList contactList = new ContactList();
        contactList.notifyUserImgChanged(email);
    }

    @Override
    public void receiveSendingRequest(String email) throws RemoteException {
        System.out.println("receiving method at user implementation  this email " + email + " rejected your request ");
        ContactList contactList = new ContactList();
        contactList.notifySendingRequest(email);
    }

    @Override
    public void receiveAcceptingRequest(String email) throws RemoteException {
        System.out.println("receiving method at user implementation Accept your Request  " + email);
        ContactList contactList = new ContactList();
        contactList.notifyAcceptingRequest(email);
    }

    @Override
    public void receiveRejectingRequest(String email) throws RemoteException {
        System.out.println("receiving method at user implementation IMG CHANGED for email  " + email);
        ContactList contactList = new ContactList();
        contactList.notifyRejectingRequest(email);
    }

    @Override
    public void receiveStatusMode(int status, String email) throws RemoteException {
        System.out.println("receiving method at user implementation class status is " + status + " email is " + email);

        ContactList contactList = new ContactList();

        contactList.notifyStatustoOther(email, status);
    }

    @Override
    public void receiveUserLogout(String email) throws RemoteException {
        System.out.println("receiving USER logout method at user implementation  email is " + email);

        ContactList contactList = new ContactList();

        contactList.notifyUserLogouttoOther(email);
    }

    @Override
    public boolean haveChatWindow(Vector<ClientInterface> senderObj) throws RemoteException {

        boolean result = false;
        Iterator<Vector<ClientInterface>> keySetIterator = chatWindows.keySet().iterator();

        System.out.println(chatWindows.size());

        while (keySetIterator.hasNext()) {
            Vector<ClientInterface> key = keySetIterator.next();
            if (key.containsAll(senderObj)) {
                result = true;
            }
        }

        return result;

    }

    public static boolean removeFromChatWindows(ChatWindow deletedChatWindow) {
        Iterator<ChatWindow> chatWindowsIterator = chatWindows.values().iterator();
        while (chatWindowsIterator.hasNext()) {
            ChatWindow currWindow = chatWindowsIterator.next();
            if (currWindow.equals(deletedChatWindow)) {
                chatWindowsIterator.remove();
                System.out.print("removed");
            }
        }
        return false;
    }

    @Override
    public boolean startSendFileWindow(File pathAtSender, String senderName, ClientInterface senderObj, ClientInterface receiverObj, double totalSize) throws RemoteException {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("File Message");
            alert.setHeaderText("Look, " + senderName + " Wants to send you  file");
            alert.setContentText("File name is : " + pathAtSender.getName());

            ButtonType acceptBtn = new ButtonType("Accept");
            ButtonType refuseBtn = new ButtonType("Refuse", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(acceptBtn, refuseBtn);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == acceptBtn) {
                try {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("File recived from : " + senderName);
                    fileChooser.setInitialFileName(pathAtSender.getName());

                    System.out.println(pathAtSender.getName());
                    File file = fileChooser.showSaveDialog(ChatClient.primaryStage);
                    senderObj.dowenloadFile(pathAtSender, file, senderName, receiverObj, totalSize);
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (result.get() == refuseBtn) {

            }

        });

        return true;
    }

    @Override
    public boolean dowenloadFile(File pathAtSender, File pathAtReceiver, String senderName, ClientInterface receiverObj, double totalSize) {

        try {
            FileInputStream in = new FileInputStream(pathAtSender);
            new Thread() {
                @Override
                public void run() {
                    try {
                        byte[] mydata = new byte[1024 * 1024];
                        int mylen;
                        mylen = in.read(mydata);
                        receiverObj.startBrogBar(pathAtReceiver);
                        Utilities.myClientImp.startBrogBar(pathAtSender);
                        double sendedLen = mylen;
                        while (mylen > 0) {
                            receiverObj.sendFileToClient(pathAtReceiver, mydata, mylen, senderName);
                            mylen = in.read(mydata);
                            sendedLen += mylen;
                            receiverObj.updateBrogBar(sendedLen, totalSize, "receiving .... " + pathAtReceiver.getName() + "\nFrom :" + senderName, pathAtReceiver);
                            Utilities.myClientImp.updateBrogBar(sendedLen, totalSize, "Sending .... " + pathAtSender.getName(), pathAtSender);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return true;
        }
    }

    @Override
    public boolean sendFileToClient(File pathAtClient, byte[] data, int len, String senderName) throws RemoteException {

        try {
            if (pathAtClient != null) {
                pathAtClient.createNewFile();
                FileOutputStream out = new FileOutputStream(pathAtClient, true);
                out.write(data, 0, len);
                out.flush();
                out.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public User getUser() throws RemoteException {
        return Utilities.user;
    }

    @Override
    public ProgBar startBrogBar(File fileName) throws RemoteException {
        ProgBar myProg = new ProgBar();
        progBars.put(fileName, myProg);
        return myProg;
    }

    @Override
    public boolean updateBrogBar(double value, double totalLength, String msg, File file) {
        Platform.runLater(() -> {
            Iterator<File> progBarIterator = progBars.keySet().iterator();
            while (progBarIterator.hasNext()) {
                File key = progBarIterator.next();
                if (key.equals(file)) {
                    progBars.get(key).updateBar(value, totalLength, msg);
                }
            }
        });
        return true;
    }

    @Override
    public void sendOnlineAnnounce(String msg){
        Platform.runLater(()->{
                    Notification.Notifier.INSTANCE.notifyInfo("Announcement From Server",msg);

        });

    }

}
