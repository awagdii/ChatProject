/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import com.server.model.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Nada
 */
public class OnlineFactory implements Callback<ListView<User>, ListCell<User>> {

    ChatWindow currentChatWindow;
    public OnlineFactory(ChatWindow currentChatWindow) {
        this.currentChatWindow= currentChatWindow;
    }

    @Override
    public ListCell<User> call(ListView<User> param) {
        return new OnlineCellFactory(this.currentChatWindow);
    }
    
}
