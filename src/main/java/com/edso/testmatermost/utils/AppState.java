package com.edso.testmatermost.utils;

import com.edso.testmatermost.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppState {
    public static boolean isSendMessage = true;
    public static final int MAX_USERS = 1000;
    public static List<User> users;
    public static BufferedWriter fileLogger;
    public static int messageId = 0;

    public static void loadUserData(){
        users = new ArrayList<>(1000);
        try{
            BufferedReader br = new BufferedReader(new FileReader("user.csv"));
            String line;
            int currentNumUser = 0;
            while((line = br.readLine()) != null && currentNumUser <= MAX_USERS){
                String[] values = line.split(",");
                User user = new User(values[0], values[1], values[2]);
                users.add(user);
                currentNumUser++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
