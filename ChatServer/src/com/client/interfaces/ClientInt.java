/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.interfaces;

import java.rmi.*;
public interface ClientInt extends Remote{
	void receive(String txt)throws RemoteException;
	
}