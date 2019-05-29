/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.database.DBConnection;
import com.server.interfaces.impl.ServerImplementation;
import com.server.interfaces.impl.UserImplementation;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class SendAnnounce extends AnchorPane {
    
    @FXML
    private Label label;
    
    @FXML
    TextArea textArea;
    
    @FXML
    TextField editText;
    
    @FXML
    Button sendBtn;
    
    String msg;
    
    public SendAnnounce() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/send_announce.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
            Platform.runLater(() -> {
                
                Stage myStage = new Stage();
                myStage.setScene(new Scene(this));
                myStage.show();
                
                sendBtn.setOnAction((e) -> {
                    
                    msg = editText.getText();
                    
                    try {
                        

                        
                        Connection connection = DBConnection.getConnection();
                        String query = "Select * from userinfo";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        ResultSet rs = preparedStatement.executeQuery();
                        while (rs.next()) {
                            String query2 = "INSERT INTO Chatoffline VALUES(?,?,?) ";
                            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                            preparedStatement2.setString(1, "Announcement From The Server : ");
                            preparedStatement2.setString(2, rs.getString("email"));
                            preparedStatement2.setString(3, msg);
                            preparedStatement2.executeUpdate();
                            
                            myStage.close();   
                        }
                        
                        
                        Iterator<String> keySetIterator = ServerImplementation.clients.keySet().iterator();
                        while (keySetIterator.hasNext()) {
                            String key = keySetIterator.next();
                            ServerImplementation.clients.get(key).sendOnlineAnnounce(msg);
                            new UserImplementation().removeOfflineMesgs(ServerImplementation.clients.get(key).getMail());
                            
                        }                 
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(SendAnnounce.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(SendAnnounce.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                });
                
            });
            
        } catch (IOException ex) {
            Logger.getLogger(SendAnnounce.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
