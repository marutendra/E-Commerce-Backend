package com.Learning.Seller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock_Update_DTO {
    private String productId;
    private int quantity;
}
