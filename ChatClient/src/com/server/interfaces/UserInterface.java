/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.interfaces;

import com.server.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Shall
 */
public interface UserInterface extends Remote {

    boolean addUser(User user) throws RemoteException;

    boolean removeUser(User user) throws RemoteException;

    void displayUsers() throws RemoteException;

    boolean loginUser(String email, String password) throws RemoteException;

    boolean searchRequestedEmail(String email);

    User getUserObject(String email);

    boolean sendRequest(String email, String friendEmail);

    boolean searchContactList(String email, String friendEmail);

    ArrayList<User> showRequest(String email);

    boolean AcceptRequest(String email, String friendEmail);

    ArrayList<User> retrieveFriends(String email);

    boolean setUserToOffline(String email) throws RemoteException;

    boolean setUserToOnline(String email) throws RemoteException;

    boolean rejectRequest(String email, String friendEmail);

    void saveImage(byte[] image, String email) throws RemoteException;

    boolean changeStatus(int status, String email);

    ArrayList<String> getOfflineMesgs(String email) throws RemoteException;

    boolean removeOfflineMesgs(String email) throws RemoteException;

    boolean sendOfflineMsg(String Sender, String Recevier,String msg) throws RemoteException;
    
     ArrayList<User> getOnlineFriends(String email) throws RemoteException;
     
     boolean checkMail(String mail) throws RemoteException;

}
