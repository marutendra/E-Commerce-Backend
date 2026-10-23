package com.Learning.Seller.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

// FIX: removed @Component and unused imports
@Document(collection = "PRODUCTS")
@Data
public class Product_Entity {
    @Id
    private String productId;
    private String userid;          // seller id who owns this product
    private String productName;
    private String productImage;
    private String title;
    private String description;
    private double price;
    private int quantity;           // stock available
    private LocalDate date;
}
