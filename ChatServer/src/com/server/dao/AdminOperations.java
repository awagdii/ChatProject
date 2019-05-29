/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.dao;

import com.database.DBConnection;
import com.server.model.Admin;
import com.server.model.User;
import com.server.interfaces.impl.UserImplementation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shall
 */
public class AdminOperations {
    Connection connection = DBConnection.getConnection();
   
    //----------------- Admin Login Method --------------------------------------------------------
    public boolean loginAdmin(String email, String password) {
        boolean flag = false;
        String query = "select * from chatServerAdmins where adminEmail=? AND adminPassword=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login Successful");
                return true;
            } else {
                System.out.println("Login Failed");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //----------------- Add new Admin Method ------------------------------------------------------
    public boolean addNewAdmin(Admin admin) {
        boolean flag = false;
        String adminName, adminEmail, adminPassword;
        int status;
        adminName = admin.getAdminName();
        adminEmail = admin.getAdminEmail();
        adminPassword = admin.getAdminPassword();

        String query = "insert into chatServerAdmins(adminName,adminEmail,adminPassword) values(?,?,?)";
        try {
            System.out.println("adding admin");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, adminName);
            preparedStatement.setString(2, adminEmail);
            preparedStatement.setString(3, adminPassword);

            int result = preparedStatement.executeUpdate();
            if (result != 0) {
                System.out.println("admin has been added successfully");
                flag = true;
            } else {
                System.out.println("couldn't add admin");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //----------------- Admin Check Method --------------------------------------------------------
    public boolean checIfAdminExists(String email, String password) {
        boolean flag = false;
        String query = "select * from chatServerAdmins where adminEmail=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Admin Exists");
                return true;
            } else {
                System.out.println("Admin Doesn't Exist");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }
}
