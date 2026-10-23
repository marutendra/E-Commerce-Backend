package com.Learning.User.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// FIX: removed @Component
@Document(collection = "USER")
@Data
public class UserEntity {
    @Id
    private String userId;
    private String name;
    private String contact;
    private String email;
    private String address;

    // accepted on signup, never returned in responses
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
