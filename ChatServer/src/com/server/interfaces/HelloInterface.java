package com.server.interfaces;

import com.client.interfaces.ClientInt;
import java.rmi.*;

public interface HelloInterface extends Remote{

	public void sayHello(String msg,String sender,String receiver)throws RemoteException;
	public void myClient(ClientInt cli,String name)throws RemoteException;
	

}