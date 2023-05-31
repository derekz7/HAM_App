package com.example.ham_app.untils;

import com.example.ham_app.models.Department;
import com.example.ham_app.models.News;
import com.example.ham_app.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Common {
    public static String baseUrl = "https://firstyellowpen47.conveyor.cloud/";
    private static final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
