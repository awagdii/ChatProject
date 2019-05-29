package com.server.interfaces.impl;
import com.client.interfaces.ClientInt;
import com.server.interfaces.HelloInterface;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
public class HelloImp extends UnicastRemoteObject 
		      implements HelloInterface{
         
	ClientInt cli;
	 Vector<ClientInt> cliVector = new Vector<ClientInt>();
        // Vector<String>names=new Vector<String>();
         HashMap< ClientInt,String>hmap=new HashMap<ClientInt,String>();
	public HelloImp()throws RemoteException{

	}
	
	public void  myClient(ClientInt cli,String name){
		//cliVector.add(cli);
            hmap.put(cli,name);
                
		
	}
	public void sayHello(String txt,String sender,String reciever){
		//for(ClientInt cli : cliVector){
            Vector<ClientInt> cliVector = new Vector<ClientInt>();
                 Set set = hmap.entrySet();
                 Iterator iterator = set.iterator();
                 while(iterator.hasNext()) {
                     Map.Entry mEntry = (Map.Entry)iterator.next();
                     try{
                      if(mEntry.getValue().equals(sender)){
                          ClientInt  cli=(ClientInt)mEntry.getKey();
                            System.out.println(mEntry.getValue());
                          
                          cliVector.add(cli);
                         // hmap.remove(cli);
                            //cli.receive(txt);
                       
                        }
                       if(mEntry.getValue().equals(reciever)){
                           ClientInt cli=(ClientInt)mEntry.getKey();
                            System.out.println(mEntry.getValue());
                           cliVector.add(cli);
                          // hmap.remove(cli);
                           // cli.receive(txt);
                           
                            
                        }
                       for(ClientInt cli:cliVector){
                           cli.receive(txt);
                       }
                 
                 
                    } catch (RemoteException ex) {
                        Logger.getLogger(HelloImp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
		}
                 
	
        

   

   
}