/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.interfaces.impl;

import com.database.DBConnection;
import com.server.dao.UserOperations;
import com.server.model.User;
import com.server.interfaces.UserInterface;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import oracle.jdbc.driver.OracleDriver;

/**
 *
 * @author Shall
 */
public class UserImplementation extends UnicastRemoteObject implements UserInterface {

    Connection connection;

    /*public static void main(String[] agrs) {

        try {
           
            UserImplementation userImplementation = new UserImplementation();
            User user = new User( "Nada", "Aya , nada", "honga@jets.com", "shall", "mix", "mars", 1);
           // userImplementation.loginUser("honga@jets.com","shall");
        } catch (RemoteException ex) {
            Logger.getLogger(UserImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }*/
    public UserImplementation() throws RemoteException {
        //DBnConnection db = new DBConnection();
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean addUser(User user) {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.addNewUser(user);
        return result;
    }

    @Override
    public boolean removeUser(User user) {

        return false;
    }

    @Override
    public void displayUsers() {
        System.out.println("hi");
    }

    @Override
    public boolean loginUser(String email, String password) {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.loginUser(email, password);
         userOps.setUserToOnline(email);
        return result;
    }

    @Override
    public boolean setUserToOffline(String email) throws RemoteException {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.setUserToOffline(email);
        return result;
    }

    @Override
    public boolean setUserToOnline(String email) throws RemoteException {
        UserOperations userOps = new UserOperations();
        System.out.println("in setUserToOnline Method");
        boolean result = userOps.setUserToOnline(email);
        return result;
    }

    //---------------------------- Integration V1.0 -----------------------------
    @Override
    public boolean searchRequestedEmail(String email) {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.searchRequestedEmail(email);
        return result;
    }

    @Override
    public User getUserObject(String email) throws RemoteException {
        UserOperations userOps = new UserOperations();
        User usr = userOps.getUserObject(email);
        return usr;
    }

    @Override
    public boolean sendRequest(String email, String friendEmail) throws RemoteException {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.sendRequest(email, friendEmail);
        return result;
    }

    @Override
    public ArrayList<User> showRequest(String email) throws RemoteException {
        UserOperations userOps = new UserOperations();
        ArrayList<User> result = userOps.showRequest(email);
        return result;
    }

    @Override
    public boolean AcceptRequest(String email, String friendEmail) throws RemoteException {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.acceptRequest(email, friendEmail);
        return result;
    }

    @Override
    public boolean searchContactList(String email, String friendEmail) throws RemoteException {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.searchContactList(email, friendEmail);
        return result;
    }

    @Override
    public ArrayList<User> retrieveFriends(String email) throws RemoteException {
        UserOperations userOps = new UserOperations();
        System.out.println("user Imple");
        ArrayList<User> myFriends = userOps.retrieveFriends(email);
        return myFriends;
    }

    @Override
    public boolean rejectRequest(String email, String friendEmail) throws RemoteException {
        UserOperations userOps = new UserOperations();
        boolean result = userOps.rejectRequest(email, friendEmail);
        return result;
    }

    @Override
    public void saveImage(byte[] image, String email) throws RemoteException {
        UserOperations usrOps = new UserOperations();
        usrOps.saveImage(image, email);
    }

    @Override
    public boolean changeStatus(int status, String email) throws RemoteException {
        UserOperations usrOps = new UserOperations();
        boolean result = usrOps.changeStatus(status, email);
        return  result;
    }
    
        @Override
    public ArrayList<String> getOfflineMesgs(String email) throws RemoteException {
        UserOperations usrOps = new UserOperations();
        ArrayList<String> result = usrOps.getOfflineMesgs(email);
        return result;

    }

    @Override
    public boolean removeOfflineMesgs(String email) throws RemoteException {

        UserOperations usrOps = new UserOperations();
        boolean result = usrOps.removeOfflineMesgs( email) ;
        return result;

    }

    @Override
    public boolean sendOfflineMsg(String Sender, String Recevier ,String msg) throws RemoteException {
        UserOperations usrOps = new UserOperations();
        boolean result = usrOps.sendOfflineMsg( Sender,  Recevier ,msg);
        return result;
    }

    @Override
    public ArrayList<User> getOnlineFriends(String email) throws RemoteException {
        UserOperations usrOps=new UserOperations();
        ArrayList<User> onlineFriends=usrOps.getOnlineFriends(email);
        return onlineFriends;
        
    }
          @Override
    public boolean checkMail(String mail) throws RemoteException {
        UserOperations userOps=new UserOperations();
        boolean notExist=userOps.checkMail(mail); //false if found and true if not found 
        return notExist;
       
    }
}
