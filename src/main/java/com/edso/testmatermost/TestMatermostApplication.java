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

    private static int numInstance = 0;
    private static int numThread = 50;

    public static void main(String[] args) throws IOException{

        SpringApplication.run(TestMatermostApplication.class, args);
        try {
            numInstance = Integer.parseInt(args[0]);
            numThread = Integer.parseInt(args[1]);
        } catch (Exception e) {
            log.info("Dau vao khong hop le. mac dinh day la may: " + numInstance + ", so thread la "+ numThread);
        }
		AppState.loadUserData();
        AppState.fileLogger = new BufferedWriter(new FileWriter("log.csv"));
        createReceiver();
		createSender();
    }

    static void createReceiver() throws IOException {
        for (int i = numInstance * numThread; i < Math.min(numThread + numInstance * numThread, AppState.MAX_USERS); i++) {
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

        for (int i = numInstance * numThread; i < Math.min(numThread + numInstance * numThread, AppState.MAX_USERS); i++) {
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
