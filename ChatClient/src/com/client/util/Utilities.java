/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.util;

import com.client.interfaces.ClientInterface;
import com.client.interfaces.impl.ClientImplementation;
import com.server.interfaces.ServerInterface;
import com.server.interfaces.UserInterface;
import com.server.model.User;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Shallw
 */
public class Utilities {

    public static User user = new User();
    public static ClientImplementation myClientImp;
    
    

    public static ServerInterface serverInterface;
    public static UserInterface userInterface;
    public static String serverIP;

    private static Pattern pattern;
    private static Matcher matcher;

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static AtomicReference<File> prevFile = new AtomicReference<File>();

    public static boolean isValidEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
