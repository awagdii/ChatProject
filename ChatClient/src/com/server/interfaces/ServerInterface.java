/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.interfaces;

import com.client.interfaces.ClientInterface;
import com.server.model.Server;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Shall
 */
public interface ServerInterface extends Remote {

    boolean openServer(Server server) throws RemoteException;

    boolean closeServer(Server server) throws RemoteException;

    boolean checkServer(Server server) throws RemoteException;

    public void registerUser(String userMail, ClientInterface clientInterface) throws RemoteException;

    void unRegisterUser(String userMail,ClientInterface clientInterface) throws RemoteException;

    void initiateExitDialog(String message) throws RemoteException;

    ClientInterface askForConn(String reciverMail) throws RemoteException;

    public void registerContactList(String email) throws RemoteException;

    public ArrayList<String> tellOtherUserLogin(String email) throws RemoteException;

    public ArrayList<String> tellOtherUserLogout(String email) throws RemoteException;

    public void tellOtherSendingRequest(String email, String friendEmail) throws RemoteException;

    public void tellOtherRequestAccepted(String email, String friendEmail) throws RemoteException;

    public void tellOtherRequestRejected(String email, String friendEmail) throws RemoteException;

    public ArrayList<String> tellOtherUserImgChanged(String email) throws RemoteException;

    public ArrayList<String> tellOtherStatusModeChanged(int status, String email) throws RemoteException;
}
