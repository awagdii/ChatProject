/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.dao;

import com.database.DBConnection;
import com.server.model.Server;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shall
 */
public class ServerOperations {

    Connection connection = DBConnection.getConnection();
    //HashMap<String, ClientInterface> contactListStatus= new HashMap<String,ClientInterface>();
    ArrayList<String> contactListStatus = new ArrayList<String>();

    public boolean checkServer(Connection connection, Server server) {
        int serverStatus = 0;
        String query = "select * from chatserver where serverId=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, server.getServerId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                serverStatus = resultSet.getInt("serverStatus");
                if (serverStatus != 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean closeServer(Connection connection, Server server) {
        String query = "update chatserver set serverStatus=? where serverId=?";
        int test = 0;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, server.getServerId());
            test = preparedStatement.executeUpdate();
            if (test == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean openServer(Connection connection, Server server) {
        String query = "update chatserver set serverStatus=? where serverId=?";
        int test = 0;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, server.getServerId());
            test = preparedStatement.executeUpdate();
            if (test == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ArrayList<String> registerContactList(String email) {
        String query = "select friendmail from contactlist where usermail=? AND request = 1";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String friendRequest = resultSet.getString("friendMail");
                contactListStatus.add(friendRequest);
            }
            System.out.println("contact list added to the Arraylist serverOperations Class");
        } catch (SQLException ex) {
            Logger.getLogger(ServerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contactListStatus;
    }
}
