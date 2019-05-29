/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import java.io.Serializable;

/**
 *
 * @author Shall
 */
public class Server implements  Serializable{
    
    private int serverId,serverStatus;

    public Server() {
    }

    public Server(int serverId, int serverStatus) {
        this.serverId = serverId;
        this.serverStatus = serverStatus;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }
    
    
}
