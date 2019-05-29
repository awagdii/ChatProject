/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import com.client.util.Utilities;
import static java.awt.SystemColor.text;
import java.io.File;
import java.io.Serializable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Ahmed
 */
public class ProgBar extends AnchorPane implements Serializable{

    @FXML
    ProgressBar pb;
    @FXML
    ProgressIndicator pi;
    @FXML
    Button hideBtn;
    @FXML
    Text textLable;
    Stage myStage;
    
    static ProgBar curr ;

    public ProgBar() {
        Platform.runLater(() -> {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/prog_bar.fxml"));
                fxmlLoader.setRoot(this);
                fxmlLoader.setController(this);
                fxmlLoader.load();
                myStage = new Stage();
                myStage.setScene(new Scene(this));
                myStage.show();
                
                hideBtn.setOnAction((e)->{
                    myStage.hide();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            curr=this;
        });

    }

    public void updateBar(double value, double totalLength, String msg) {
            pb.setProgress(value / totalLength);
            pi.setProgress(value / totalLength);
            textLable.setText(msg);            
    }
    public void exitBar(){
        myStage.close();
    }
}
