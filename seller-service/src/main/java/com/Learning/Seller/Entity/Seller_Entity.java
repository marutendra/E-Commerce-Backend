package com.Learning.Seller.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// FIX: removed @Component. Entities are data objects, not Spring beans.
@Document(collection = "SELLER_INFO")
@Data
public class Seller_Entity {
    @Id
    private String userID;
    private String name;
    private String contact;
    private String email;

    // FIX: WRITE_ONLY means the password is accepted on signup
    // but is never sent back in any JSON response.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
