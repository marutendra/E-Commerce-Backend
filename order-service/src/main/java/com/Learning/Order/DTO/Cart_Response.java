package com.Learning.Order.DTO;

import lombok.Data;

import java.util.List;

// What the User service returns for GET /user/mycart/{userid}
// (only the fields Order service needs; extra JSON fields are ignored)
@Data
public class Cart_Response {
    private String userId;
    private List<Cart_Item_Response> items;
}
