package com.Learning.Seller.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// FIX: removed @Component
@Document(collection = "PROFILE")
@Data
public class Profile_Entity {
    @Id
    private String profileID;
    private String shop;
    private String gst;
    private String address;
    private String description;
    @DBRef
    private Seller_Entity sellerEntity;
}
