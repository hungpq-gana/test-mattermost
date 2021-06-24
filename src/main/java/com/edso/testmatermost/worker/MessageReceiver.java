package com.edso.testmatermost.worker;

import com.edso.testmatermost.model.User;
import com.edso.testmatermost.repository.UserClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;


import java.net.URI;

public class MessageReceiver implements Runnable {

    private final String token;

    public MessageReceiver(User user) {
        UserClient userClient = new UserClient();
        token = userClient.login(user);
    }

    @Override
    public void run() {
        URI uri = URI.create("ws://192.168.50.33:8065/api/v4/websocket");
        WebSocketClient client = new WebSocketClient();
        try {
            client.start();
            SocketEventHandler handler = new SocketEventHandler();
            Session session = client.connect(handler, uri).get();
            String msg = "{ \"seq\": 1, \"action\": \"authentication_challenge\", \"data\": { \"token\": \"" + token + "\" } }";
            session.getRemote().sendString(msg);
            handler.awaitClosure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
