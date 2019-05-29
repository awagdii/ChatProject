/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import java.io.Serializable;
import javafx.scene.image.Image;

/**
 *
 * @author Shall
 */
public class User implements Serializable{
    String firstName,lastName,eMail,password,gender,country;
    int status, realStatus;
    byte[] userImage;

    public User() {
    }
/*
    public User(String firstName,  String eMail,String gender, String country) {
        this.firstName = firstName;
        this.eMail = eMail;
        this.gender = gender;
        this.country = country;
     }
  */  
    public User(String firstName, String lastName, String eMail, String password, String gender, String country, int  status,byte[] image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.status = status;
        this.userImage=image;
      //  this.realStatus=realStatus;
    }

    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
        
    public byte[] getImage(){
        return userImage;
    }
    
    public void setImage(byte[] i){
        this.userImage=i;
    }

    public int getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(int realStatus) {
        this.realStatus = realStatus;
    }
      
}
