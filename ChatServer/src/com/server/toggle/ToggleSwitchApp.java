/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.toggle;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Shall
 */
public class ToggleSwitchApp extends Application implements  ChangeListener{
    ToggleSwitch toggle;
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(300, 300);
        toggle = new ToggleSwitch();
        Text text = new Text();
        Button button = new Button("hi");
        Pane p = new Pane();
        p.setPrefSize(200, 200);
       
        button.textProperty().bind(Bindings.when(toggle.getSwitchOn()).then("Yes").otherwise("No"));
        toggle.getSwitchOn().addListener(this);
     
        //---------------------------------------------------
        // button.onActionProperty().bind(Bindings.when(toggle.getSwitchOn()).then((event)->{}));
        BooleanProperty flagProperty = toggle.getSwitchOn();
        //setonmo
        button.onActionProperty().addListener((property, oldState, newState) -> {
            boolean flag = toggle.getSwitchOn().getValue();
            if(flag)
                button.setText("On");
            else
                button.setText("Off");
        });
        //--------------------------------------------------------------------------
        root.getChildren().addAll(toggle, button,text);
        toggle.setTranslateX(100);
        toggle.setTranslateY(200);
        return root;
    }
    public static String printy(){
        System.out.println("hello");
        return "";
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(toggle.getSwitchOn().getValue())
             System.out.println("Electric bill has changed!");
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
