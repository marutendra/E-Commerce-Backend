package com.Learning.Order.Feign;

import com.Learning.Order.DTO.Cart_Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// "USER-SERVICE" = spring.application.name=User-Service in the User service
@FeignClient(name = "USER-SERVICE")
public interface User_Client {

    @GetMapping("/user/mycart/{userid}")
    Cart_Response getCart(@PathVariable("userid") String userid);

    @DeleteMapping("/user/clearcart/{userid}")
    void clearCart(@PathVariable("userid") String userid);
}
