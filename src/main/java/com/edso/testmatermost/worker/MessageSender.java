package com.edso.testmatermost.worker;

import com.edso.testmatermost.model.User;
import com.edso.testmatermost.repository.UserClient;
import com.edso.testmatermost.utils.AppState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MessageSender implements Runnable {

    private final User user;

    @Override
    public void run() {
        UserClient userClient = new UserClient();
        String token = userClient.login(user);
        while (AppState.isSendMessage) {
            int randNum = (int) (Math.random() * 1000);
            User receiveUser = AppState.users.get(randNum);
            String channelID = userClient.createChannel(token, user.getUid(), receiveUser.getUid());
            String message = createMessage(user);
            long sendTime = System.currentTimeMillis();
            log.info("Send message " + Thread.currentThread().getName() + " at: " + System.currentTimeMillis());
            userClient.sendMessage(token, channelID, message);
            writeLog(message, sendTime);
            try {
                long timeSleep = (int) (Math.random() * 20 + 10);
                Thread.sleep(timeSleep * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String createMessage(User user1) {
        StringBuilder message = new StringBuilder();
        message.append(user1.getUid()).append("_").append(AppState.messageId++).append(",");
        int messageLength = (int) (Math.random() * 100 + 5);
        for (int i = 0; i < messageLength; i++) {
            int charId = (int) (Math.random() * 60 + 65);
            message.append((char) charId);
        }
        return message.toString();
    }

    private void writeLog(String message, long sendTime) {
        try {
            AppState.fileLogger.write(message + "," + sendTime + ",0\n");
            AppState.fileLogger.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
