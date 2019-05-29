/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.interfaces.impl;

import com.client.interfaces.ClientInterface;
import com.database.DBConnection;
import com.server.dao.ServerOperations;
import com.server.dao.UserOperations;
import com.server.interfaces.ServerInterface;
import com.server.model.Server;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shall
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    Connection connection;
    ServerOperations serverOperations;
    public static HashMap<String, ClientInterface> clients = new HashMap<String, ClientInterface>();// ALL THE USER ONLINE
    HashMap<String, ClientInterface> contactliststatus = new HashMap<String, ClientInterface>(); // my friend online
    ArrayList<String> contactListArrayList = new ArrayList<String>();

    public ServerImplementation() throws RemoteException {
        connection = DBConnection.getConnection();
        serverOperations = new ServerOperations();
    }

    @Override
    public boolean openServer(Server server) throws RemoteException {
        if (!serverOperations.checkServer(connection, server)) {
            System.out.println("Couldn't open server");
        }
        return serverOperations.openServer(connection, server);
    }

    @Override
    public boolean closeServer(Server server) throws RemoteException {
        if (!serverOperations.closeServer(connection, server)) {
            System.out.println("Couldn't close server");
        }
        return serverOperations.closeServer(connection, server);
    }

    @Override
    public boolean checkServer(Server server) throws RemoteException {
        if (!serverOperations.checkServer(connection, server)) {
            System.out.println("Server Closed");
        }
        return serverOperations.checkServer(connection, server);
    }

    @Override
    public void registerUser(String userMail, ClientInterface clientInterface) {
        clients.put(userMail, clientInterface);
        //    clients.add(userMail,clientInterface);
        System.out.println("Client added   " + userMail);
    }

    @Override
    public void unRegisterUser(String userMail,ClientInterface clientInterface) {
        //  clients.remove(clientInterface);
        System.out.println("Client removed");
        UserOperations userOps = new UserOperations();
        userOps.setUserToOffline(userMail);
        System.out.println("Client removed");
        clients.remove(userMail, clientInterface);

    }

    @Override
    public void initiateExitDialog(String message) throws RemoteException {
//        for (ClientInterface clients : clients.) {
//            client.displayMessgaeInDialog(message);
//        }
            System.out.println("initiateExitDialog ServerImplementation map size: "+clients.size());
          for(Map.Entry<String,ClientInterface>entry:clients.entrySet()){
              String key = entry.getKey();
              ClientInterface client = entry.getValue();
              client.displayMessgaeInDialog(message);
              clients.remove(key);
          }
    }

    @Override
    public ClientInterface askForConn(String reciverMail) throws RemoteException {
        System.out.println("ask for conection function ");

        Iterator<String> keySetIterator = clients.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            if (key.equals(reciverMail)) {
                System.out.println("Sended Sucsessfully ");
                return clients.get(key);
            }
            System.out.println("couldn't found the user");
        }

        return null;
    }

   
    @Override
    public void registerContactList(String email) throws RemoteException {
        ServerOperations operations = new ServerOperations();
        contactListArrayList = operations.registerContactList(email);
        for (int i = 0; i < contactListArrayList.size(); i++) {
            String s = contactListArrayList.get(i);
            if (clients.containsKey(s)) {
                ClientInterface c = clients.get(s);
                contactliststatus.put(s, c);
                System.out.println("server implementation class registerContactlist " + s);
            }
        }
    }

    @Override
    public ArrayList<String> tellOtherUserLogin(String email) throws RemoteException {
        for (Map.Entry<String, ClientInterface> map : contactliststatus.entrySet()) {
            String mail = map.getKey();
            ClientInterface c = map.getValue();
            c.receiveUserLogin(email);
        }
        return contactListArrayList;
    }

    @Override
    public void tellOtherSendingRequest(String email, String friendEmail) throws RemoteException {
        for (Map.Entry<String, ClientInterface> map : clients.entrySet()) {
            if (map.getKey().equals(friendEmail)) {
                System.out.println("friend online");
                ClientInterface c = map.getValue();
                c.receiveSendingRequest(email);
            }
        }
    }

    @Override
    public void tellOtherRequestAccepted(String email, String friendEmail) throws RemoteException {
        System.out.println("tell other request Accepted freind email: " + friendEmail);
        for (Map.Entry<String, ClientInterface> map : clients.entrySet()) {
            if (map.getKey().equals(friendEmail)) {
                System.out.println("friend online");
                ClientInterface c = map.getValue();
                c.receiveAcceptingRequest(email);
            }
        }
    }

    @Override
    public void tellOtherRequestRejected(String email, String friendEmail) throws RemoteException {
        System.out.println("tell other request Rejected freind email: " + friendEmail);
        for (Map.Entry<String, ClientInterface> map : clients.entrySet()) {
            if (map.getKey().equals(friendEmail)) {
                System.out.println("friend online");
                ClientInterface c = map.getValue();
                c.receiveRejectingRequest(email);
            }
        }
    }

    @Override
    public ArrayList<String> tellOtherUserImgChanged(String email) throws RemoteException {
        for (Map.Entry<String, ClientInterface> map : contactliststatus.entrySet()) {
            String mail = map.getKey();
            ClientInterface c = map.getValue();
            c.receiveUserImgChanged(email);
        }
        return contactListArrayList;
    }

    @Override
    public ArrayList<String> tellOtherStatusModeChanged(int status, String email) throws RemoteException {
        for (Map.Entry<String, ClientInterface> map : contactliststatus.entrySet()) {
            String mail = map.getKey();
            ClientInterface c = map.getValue();
            c.receiveStatusMode(status, email);
        }
        return contactListArrayList;
    }

    @Override
    public ArrayList<String> tellOtherUserLogout(String email) throws RemoteException {
        for (Map.Entry<String, ClientInterface> map : contactliststatus.entrySet()) {
            String mail = map.getKey();
            if (mail != email) {
                ClientInterface c = map.getValue();
                c.receiveUserLogout(email);
            }
        }
        return contactListArrayList;
    }

}
