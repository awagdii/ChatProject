/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.*;

/**
 *
 * @author Shall
 */
public class DBConnection {

    static volatile Connection con = null;

    static {
        try {

            DriverManager.registerDriver(new OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "jets", "java");
//            java.sql.Statement stmt = con.createStatement();
//            String query = "CREATE TABLE CHATOFFLINE "
//                    + "(sender VARCHAR2(200) NOT NULL ENABLE, "
//                    + " receiver VARCHAR2(200) NOT NULL ENABLE, "
//                    + " msg CLOB ) ";
//            stmt.execute(query);
            System.out.println("db initiated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getConnection() {
        try {
            if (con.isClosed()) {
                con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "jets", "java");
            }
            return con;
        } catch (SQLException ex) {
            try {
                //Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "jets", "java");
            } catch (SQLException ex1) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return con;
    }

    public synchronized void closeConnection(Connection con) {

        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
