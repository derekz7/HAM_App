package com.example.ham_app.untils;

import com.example.ham_app.models.User;

import java.util.regex.Pattern;

public class Common {
    public static String baseUrl = "https://othershinyhen78.conveyor.cloud/";
    public static User currentUserLogin;

    private static final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
