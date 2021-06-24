package com.edso.testmatermost.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Data
public class User {

    @JsonProperty("login_id")
    private String email;
    private String password;
    private String uid;
//    private String token;
}
