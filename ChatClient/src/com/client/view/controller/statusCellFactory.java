/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 *
 * @author aya
 */
public class statusCellFactory implements Callback<ListView<Text>, ListCell<Text>> {

    @Override
    public ListCell<Text> call(ListView<Text> param) {

        return new ListCell<Text>() {
            private final Text t;
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                t=new Text();
            }

            @Override
            protected void updateItem(Text item, boolean empty) {
                super.updateItem(item, empty); 
                if(item==null||empty){
                    setGraphic(null);
                }else{
                    t.setText(item.getText());
                    setGraphic(t);
                }
            }       
        };
    }
}
