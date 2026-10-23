package com.Learning.Order.DTO;

import lombok.Data;

// Field names must match Return_type_dto of the User service
@Data
public class Cart_Item_Response {
    private String productid;
    private String productName;
    private int quantity;
}
