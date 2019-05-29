/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.dao;

import com.database.DBConnection;
import com.server.model.User;
import com.server.interfaces.impl.UserImplementation;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
//import javafx.scene.image.Image;

/**
 *
 * @author Shall
 */
public class UserOperations {

    Connection connection = DBConnection.getConnection();

    //-------------------- Add New User Method -------------------------------------------------------
    public boolean addNewUser(User user) {
        boolean flag = false;
        String firstName, lastName, eMail, password, gender, country;
        int status;
        firstName = user.getFirstName();
        lastName = user.getLastName();
        eMail = user.geteMail();
        password = user.getPassword();
        gender = user.getGender();
        country = user.getCountry();
        status = 0;
        FileInputStream im;
        String query = "insert into UserInfo(email,password,firstName,lastName,gender,status,country,userImage) values(?,?,?,?,?,?,?,?)";
        try {
            System.out.println("adding user");
            im=new FileInputStream("src/img/pic.jpg");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, eMail);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, gender);
            preparedStatement.setInt(6, status);
            preparedStatement.setString(7, country);
            preparedStatement.setBinaryStream(8, im,im.available());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("user has been added successfully");
                flag = true;
            } else if (result == 0) {
                System.out.println("couldn't add user");

            } else {
                System.out.println("couldn't add user");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //-------------------- User Login Validation Method ----------------------------------------------
    public boolean loginUser(String email, String password) {
        boolean flag = false;
        String query = "select * from UserInfo where email=? AND password=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login Successful");
                return true;
            } else {
                System.out.println("Login Failed");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //-------------------- Method that changes user status to offline --------------------------------
    public boolean setUserToOffline(String email) {
        String query = "update userInfo set realstatus=2 where email=?";
        int test = 0;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            test = preparedStatement.executeUpdate();
            if (test == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //-------------------- Method that changes user status to online ---------------------------------
    public boolean setUserToOnline(String email) {
        String query = "update userInfo set realstatus=1 where email=?";
        int test = 0;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            test = preparedStatement.executeUpdate();
            if (test == 0) {
                System.out.println("Couldn't set user to be online");
                return false;
            } else {
                System.out.println("User is online successfully");
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //--------------------------- Search if the Email is a real user----------------------------------
    public boolean searchRequestedEmail(String email) {
        boolean flag = false;
        String query = "select * from UserInfo where email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                sendRequest("test", email);
                System.out.println("searchRequestedEmail user info Found");
                return true;
            } else {
                System.out.println("searchRequestedEmail userinfo not Found");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;

    }

    //---------------------------Check if he is already friend or not-----------------------------------
    public boolean searchContactList(String email, String friendEmail) {
        boolean flag = false;
        String query = "select friendmail from ContactList where usermail=? AND friendmail=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, friendEmail);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
              System.out.println(resultSet.getString("friendmail"));
                System.out.println("searchContactList Found");
                flag = false;
            } else {
                System.out.println("searchContactList not Found");
                flag = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;

    }

    //---------------------------get info about user added to contact List-----------------------------------
    public User getUserObject(String email) {
        User usr = new User();
        String firstName = "";
        String lastName = "";
        String query = "select * from UserInfo where email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // resultSet.getString("EMAIL");
                firstName = resultSet.getString("FIRSTNAME");
                lastName = resultSet.getString("LASTNAME");

                usr.setFirstName(firstName);
                usr.setLastName(lastName);
                usr.seteMail(email);
                usr.setGender(resultSet.getString("Gender"));
                usr.setStatus(resultSet.getInt("status"));
                usr.setImage(resultSet.getBytes("userImage"));
                System.out.println("userOperations DB" + usr.getLastName() + "," + usr.getFirstName() + "," + usr.geteMail());

            } else {
                System.out.println("not Found");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usr;

    }

    //---------------------------send Request to user Add him in ContactList table-----------------------------------
    public boolean sendRequest(String email, String friendEmail) {
        System.out.println("useroperation sendrequest fun");

        boolean flag = false;
        String query = "insert into ContactList values (?,?,SYSDATE,0,?)";
        String query2 = "insert into ContactList values (?,?,SYSDATE,0,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, friendEmail);
            preparedStatement.setString(3, email);

            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setString(1, friendEmail);
            preparedStatement2.setString(2, email);
            preparedStatement2.setString(3, email);

            System.out.println("try useroperation sendrequest fun");

            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            System.out.println("inserted Successfully");
            flag = true;
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //---------------------------Show the Add Request-----------------------------------
    public ArrayList<User> showRequest(String email) {
        ArrayList<User> friendRequestArr = new ArrayList<>();
        String query = "select * from ContactList  where userMail=? AND REQUEST=0";
        System.out.println(query);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String friendRequest = resultSet.getString("friendMail");
                User friendRequestUserObj = getUserObject(friendRequest);
                friendRequestArr.add(friendRequestUserObj);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friendRequestArr;
    }

    //---------------------------Accept the Request Update Request Col in ContactList to 1-----------------------------------
    public boolean acceptRequest(String email, String friendEmail) {
        boolean flag = false;
        String query = "UPDATE ContactList  SET REQUEST=1"
                + "WHERE userMail=? AND friendMail=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, friendEmail);
            PreparedStatement preparedStatement2 = connection.prepareStatement(query);
            preparedStatement2.setString(1, friendEmail);
            preparedStatement2.setString(2, email);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //----------------------------New work  (25/1/2016) ---------------------------------------
    /*----------------------------Get number of online users Method----------------------------
       This method will retrieve Number of online users through their status
       If status = 1 then user is online
    ------------------------------------------------------------------------------------------*/
    public int getNumberOfOnlineUsers() {
        int numberOfOnlineUsers = 0;
        String query = "select * from UserInfo where Realstatus = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 1);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                numberOfOnlineUsers++;
            }
            resultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfOnlineUsers;
    }

    /*----------------------------Get number of offline users Method----------------------------
       This method will retrieve Number of offline users through their status
       If status = 0 then user is online
    -------------------------------------------------------------------------------------------*/
    public int getNumberOfOfflineUsers() {
        int numberOfOfflineUsers = 0;
        String query = "select * from UserInfo where Realstatus = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 2);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                numberOfOfflineUsers++;
            }
            resultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfOfflineUsers;
    }

    /*----------------------------Get the total number of users Method--------------------------
       This method will retrieve total number of users
    -------------------------------------------------------------------------------------------*/
    public int getTotalNumberOfUsers() {
        int totalNumberOfUsers = 0;
        String query = "select * from UserInfo";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalNumberOfUsers++;
            }
            resultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalNumberOfUsers;
    }

    /*----------------------------Get number of female users Method-----------------------------
       This method will retrieve Number of female users through their gender
    ------------------------------------------------------------------------------------------*/
    public int getNumberOfFemaleUsers() {
        int numberOfFemaleUsers = 0;
        String query = "select * from UserInfo where gender = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "female");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                numberOfFemaleUsers++;
            }
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfFemaleUsers;
    }

    /*----------------------------Get number of male users Method-----------------------------
       This method will retrieve Number of male users through their gender
    ------------------------------------------------------------------------------------------*/
    public int getNumberOfMaleUsers() {
        int numberOfMaleUsers = 0;
        String query = "select * from UserInfo where gender = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "male");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                numberOfMaleUsers++;
            }
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfMaleUsers;
    }

    //--------------------Retrieve the User Friend From DataBase------------------------------------
    public ArrayList<User> retrieveFriends(String email) {

        ArrayList<User> myFriends = new ArrayList<User>();

        try {
            String query = "Select FirstName ,Status,Email,userImage,realStatus from UserInfo where Email IN ( select FriendMail from ContactList where userMail=? AND request=1) ORDER BY realStatus,status ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("hhhhhhh");
            while (rs.next()) {
                User myfriend = new User();
                System.out.print("retrieve data of user");
                myfriend.setFirstName(rs.getString("FirstName"));
                myfriend.setStatus(rs.getInt("Status"));
                myfriend.seteMail(rs.getString("Email"));
                myfriend.setRealStatus(rs.getInt("realStatus"));
                Blob b = rs.getBlob("userImage");
                byte image[] = b.getBytes(1, (int) b.length());
                // FileOutputStream fout;
                System.out.print("before saving the photo");
                myfriend.setImage(image);
                myFriends.add(myfriend);
                System.out.print("friend is added to view");
                for (int i = 0; i < myFriends.size(); i++) {
                    // System.out.println("ContactList");
                    System.out.println(myFriends.get(i).getFirstName());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myFriends;
    }

    public boolean rejectRequest(String email, String friendEmail) {
        boolean flag = false;
        String query = "Delete  FROM ContactList  WHERE REQUEST=0 AND userMail=? AND friendMail=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, friendEmail);
            PreparedStatement preparedStatement2 = connection.prepareStatement(query);
            preparedStatement2.setString(1, friendEmail);
            preparedStatement2.setString(2, email);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            System.out.println("reject request uer opreation");
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    public void saveImage(byte[] image, String email) {
        try {
            ByteArrayInputStream myimage;
            myimage = new ByteArrayInputStream(image);
            System.out.println(myimage.toString());
            
            String query = "update userinfo set userImage=? where email=? ";
            java.sql.PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setBinaryStream(1, myimage, myimage.available());
            stmt.setString(2, email);
            stmt.execute();
 
            System.out.println("Image was succesfully inserted in the Database");
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean changeStatus(int status,String email) {
        boolean flag = false;
        try {
            System.out.println("changing status");
            String query = "update userinfo set status=? where email=? ";
            java.sql.PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, status);
            stmt.setString(2, email);
            stmt.execute();
            flag=true;
            System.out.println("status  was succesfully changed in the Database");
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  flag;
    }
    

    //--------------------------Update V1.2 (31-1-2016)-----------------------------------------------
    /*----------------------------Get number of online users Method----------------------------
       This method will retrieve Number of Egyptian users through their country
    ------------------------------------------------------------------------------------------*/
    public int getNumberOfEgyptianUsers() {
        int numberOfEgyptianUsers = 0;
        String query = "select * from UserInfo where country = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Egypt");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                numberOfEgyptianUsers++;
            }
            resultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfEgyptianUsers;
    }

     /*----------------------------Get number of online users Method----------------------------
       This method will retrieve Number of Egyptian users through their country
    ------------------------------------------------------------------------------------------*/
    public int getNumberOfAmericanUsers() {
        int numberOfAmericanUsers = 0;
        String query = "select * from UserInfo where country = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "USA");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                numberOfAmericanUsers++;
            }
            resultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numberOfAmericanUsers;
    }
    
        public ArrayList<String> getOfflineMesgs(String email) {
        ArrayList<String> myOffmsgs = new ArrayList<String>();

        try {
            String query = "Select Sender ,msg from Chatoffline where receiver =? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String msg = rs.getString("Sender") + " :\n" + rs.getString("msg") + "\n";
                myOffmsgs.add(msg);

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return myOffmsgs;
    }

    public boolean removeOfflineMesgs(String email) {

        try {
            String query = "DELETE from Chatoffline where receiver =? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public boolean sendOfflineMsg(String Sender, String Recevier ,String msg) {
                  try {
            String query = "INSERT INTO Chatoffline VALUES(?,?,?) ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Sender);
            preparedStatement.setString(2, Recevier);
            preparedStatement.setString(3, msg);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    public ArrayList<User> getOnlineFriends(String email){
        ArrayList<User> onlineFriends=new ArrayList();
        try {
            String query = "Select FirstName ,Status,Email,userImage from UserInfo where Email IN ( select FriendMail from ContactList where userMail=? AND request=1) AND RealStatus=1 ORDER BY realStatus,status ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("hhhhhhh");
            while (rs.next()) {
                User myfriend = new User();
                myfriend.setFirstName(rs.getString("FirstName"));
                myfriend.setStatus(rs.getInt("Status"));
                myfriend.seteMail(rs.getString("Email"));
                Blob b = rs.getBlob("userImage");
                byte image[] = b.getBytes(1, (int) b.length());
                myfriend.setImage(image);
                onlineFriends.add(myfriend);
                for (int i = 0; i < onlineFriends.size(); i++) {
                    System.out.println(onlineFriends.get(i).getFirstName());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return onlineFriends;
    }
    public boolean checkMail(String mail) {
        boolean notFound = true;
         try {
             
             Vector<String>mails=new Vector<String>();
            String query = "select email from userInfo ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                mails.add(rs.getString("email"));
            }
            for(String email:mails){
                if(email.equals(mail)){
                    notFound= false;
                }
            }
           

        } catch (SQLException ex) {
            Logger.getLogger(UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notFound; 
    }
}
