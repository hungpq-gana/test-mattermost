package com.edso.testmatermost.worker;

import com.edso.testmatermost.utils.AppState;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Slf4j
@NoArgsConstructor
public class SocketEventHandler extends WebSocketAdapter {

    private final CountDownLatch closureLatch = new CountDownLatch(1);

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        log.info("Socket Connected " + sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
//        log.info("Receive text: " + message);
        processMessage(message);
    }

    public void awaitClosure() throws InterruptedException {
        closureLatch.await();
    }

    private void processMessage(String message) {
        long receiveTime = System.currentTimeMillis();
        try {
            String receiveMessage = getLogMessage(message);
            if(receiveMessage.isEmpty()) return;
            AppState.fileLogger.write(receiveMessage + ",0," + receiveTime + '\n');
            AppState.fileLogger.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLogMessage(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(message);
        JsonNode event = jsonNode.get("event");
        if (Objects.isNull(event) || !"posted".equals(event.asText())) return "";
        String post = jsonNode.get("data").get("post").asText("");
        return mapper.readTree(post).get("message").asText();
    }
}
