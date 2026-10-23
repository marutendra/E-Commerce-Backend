package com.Learning.User.Entity;

import com.Learning.User.DTO.Return_type_dto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "CART")
@Data
public class Cart_Entity {
    @Id
    private String cartId;
    private String userId;
    private List<Return_type_dto> items = new ArrayList<>();
    private double total_cart_price = 0;
}
