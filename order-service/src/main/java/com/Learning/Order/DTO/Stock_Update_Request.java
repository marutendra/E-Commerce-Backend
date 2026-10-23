package com.Learning.Order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Matches Stock_Update_DTO of the Seller service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock_Update_Request {
    private String productId;
    private int quantity;
}
