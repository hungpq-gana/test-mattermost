package com.edso.testmatermost.repository;

import com.edso.testmatermost.model.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@NoArgsConstructor
public class UserClient {

    private final String hostHttp = "http://192.168.50.33:8065/";
    private final String hostWs = "ws://192.168.50.33:8065/";
    private final RestTemplate rest = new RestTemplate();

    public String login(User user){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);
        String loginUrl = hostHttp + "api/v4/users/login";
        try{
            ResponseEntity<Map> res = rest.postForEntity(loginUrl, entity, Map.class);
            user.setUid((String) Objects.requireNonNull(res.getBody()).get("id"));
            return Objects.requireNonNull(res.getHeaders().get("Token")).get(0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int sendMessage(String token, String channelId, String message){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + token);
        Map<String, String> body = new HashMap<>();
        body.put("channel_id", channelId);
        body.put("message", message);
        HttpEntity<Map> entity = new HttpEntity<>(body, httpHeaders);
        String sendMesgageUrl = hostHttp + "api/v4/posts";
        try{
            ResponseEntity<String> res =  rest.postForEntity(sendMesgageUrl, entity, String.class);
            return res.getStatusCodeValue();
        } catch (Exception e){
            e.printStackTrace();
        }
        return 400;
    }

    public String createChannel(String token, String uid1, String uid2){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + token);
        List<String> body = new ArrayList<>();
        body.add(uid1);
        body.add(uid2);
        HttpEntity<List> entity = new HttpEntity<>(body, httpHeaders);
        String sendMesgageUrl = hostHttp + "api/v4/channels/direct";
        try{
            ResponseEntity<Map> res =  rest.postForEntity(sendMesgageUrl, entity, Map.class);
            return (String) Objects.requireNonNull(res.getBody()).get("id");
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
