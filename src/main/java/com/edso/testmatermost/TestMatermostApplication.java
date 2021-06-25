package com.edso.testmatermost;

import com.edso.testmatermost.model.User;
import com.edso.testmatermost.utils.AppState;
import com.edso.testmatermost.worker.MessageReceiver;
import com.edso.testmatermost.worker.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
@Slf4j
public class TestMatermostApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(TestMatermostApplication.class, args);
        try {
            AppState.numInstance = Integer.parseInt(args[0]);
            AppState.numThread = Integer.parseInt(args[1]);
        } catch (Exception e) {
            log.info("Dau vao khong hop le. mac dinh day la may: " + AppState.numInstance + ", so thread la " + AppState.numThread);
        }
        AppState.loadUserData();
        AppState.fileLogger = new BufferedWriter(new FileWriter("log.csv"));
        createReceiver();
        createSender();
    }

    static void createReceiver() throws IOException {
        int startUser = AppState.numInstance * AppState.numThread;
        int endUser = Math.min(AppState.numThread + startUser, AppState.MAX_USERS);
        for (int i = startUser; i < endUser; i++) {
            User user = AppState.users.get(i);
            MessageReceiver messageReceiver = new MessageReceiver(user);
            Thread thread = new Thread(messageReceiver);
            thread.start();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void createSender() {
        int startUser = AppState.numInstance * AppState.numThread;
        int endUser = Math.min(AppState.numThread + startUser, AppState.MAX_USERS);
        for (int i = startUser; i < endUser; i++) {
            MessageSender sender = new MessageSender(AppState.users.get(i));
            log.info("Create thread user: " + AppState.users.get(i).getEmail());
            Thread thread = new Thread(sender);
            thread.start();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
