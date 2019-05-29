/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.interfaces;

import com.client.view.controller.ProgBar;
import com.server.model.User;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 *
 * @author Shall
 */
public interface ClientInterface extends Remote{
   
    void displayMessgaeInDialog(String message) throws RemoteException;

    boolean sendFile(String filename, byte[] data, int len, String senderName) throws RemoteException;

    // to start the chat window at the other client
    boolean startChatWindow(Vector<ClientInterface> senderObj) throws RemoteException;
    
    boolean sendMsg( Vector<ClientInterface> senderObj,String msg)throws RemoteException;
    
    boolean haveChatWindow(Vector<ClientInterface> senderObj)throws RemoteException;
    
    String getMail()throws RemoteException;
    
    public void receiveUserLogin(String email) throws RemoteException;

    public void receiveUserLogout(String email) throws RemoteException;

    public void receiveStatusMode(int status, String email) throws RemoteException;

    public void receiveUserImgChanged(String email) throws RemoteException;

    public void receiveSendingRequest(String email) throws RemoteException;

    public void receiveAcceptingRequest(String email) throws RemoteException;

    public void receiveRejectingRequest(String email) throws RemoteException;
    
    boolean startSendFileWindow(File pathAtSender, String senderName, ClientInterface senderObj, ClientInterface receiverObj, double totalSize) throws RemoteException;

    boolean dowenloadFile(File pathAtSender, File pathAtReceiver, String senderName, ClientInterface receiverObj, double totalSize) throws RemoteException;

    boolean sendFileToClient(File pathAtClient, byte[] data, int len, String senderName) throws RemoteException;

    ProgBar startBrogBar(File file) throws RemoteException;

    boolean updateBrogBar(double value, double totalLength, String msg ,File file) throws RemoteException;

    User getUser() throws RemoteException;
    
    void sendOnlineAnnounce(String msg)throws RemoteException;
    
}
