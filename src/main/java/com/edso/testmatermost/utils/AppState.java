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
    public static int numInstance = 0;
    public static int numThread = 100;
    public static final int MAX_USERS = 100;
    public static List<User> users;
    public static BufferedWriter fileLogger;
    public static int messageId = 0;

    public static void loadUserData(){
        users = new ArrayList<>(AppState.MAX_USERS);
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
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
