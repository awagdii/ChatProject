/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Shall
 */
public class Utilities {

    private static Pattern pattern;
    private static Matcher matcher;

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateFirstName(String firstName) {

        if (firstName.isEmpty() || firstName.indexOf(" ") > 0 || containDigits(firstName) || firstName.length() < 2 || firstName.length() > 10 || firstName.length() == 0) {
            return false;
        } else {
            return true;
        }

    }

    public boolean validateLastName(String lastName) {

        if (lastName.isEmpty() || lastName.indexOf(" ") > 0 || containDigits(lastName) && lastName.length() < 2 || lastName.length() > 10 || lastName.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean containDigits(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (Character.isDigit(name.indexOf(i))) {
                return true;
            }

        }
        return false;
    }

    public boolean validatePassword(String password) {

        final String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        pattern = Pattern.compile(passwordPattern);

        matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
