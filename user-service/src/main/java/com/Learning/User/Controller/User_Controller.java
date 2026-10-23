package com.Learning.User.Controller;

import com.Learning.User.DTO.Add_Product;
import com.Learning.User.DTO.Product_Response;
import com.Learning.User.DTO.User_Login_DTO;
import com.Learning.User.Entity.Cart_Entity;
import com.Learning.User.Entity.UserEntity;
import com.Learning.User.Service.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class User_Controller {

    @Autowired
    User_service userService;

    @PostMapping("/signup")
    public UserEntity signup(@RequestBody UserEntity userEntity) {
        return userService.signup(userEntity);
    }

    // NEW
    @PostMapping("/login")
    public UserEntity login(@RequestBody User_Login_DTO dto) {
        return userService.login(dto);
    }

    @GetMapping("/show/{s}")
    public List<Product_Response> show(@PathVariable String s) {
        return userService.show(s);
    }

    // FIX: was @GetMapping with @RequestBody. Adding to a cart changes data, so it is POST.
    @PostMapping("/addproduct")
    public Cart_Entity addproduct(@RequestBody Add_Product addProduct) {
        return userService.addproduct(addProduct);
    }

    @GetMapping("/mycart/{userid}")
    public Cart_Entity getcart(@PathVariable String userid) {
        return userService.getMyCart(userid);
    }

    // NEW
    @DeleteMapping("/cart/{userid}/item/{productId}")
    public Cart_Entity removeItem(@PathVariable String userid, @PathVariable String productId) {
        return userService.removeItem(userid, productId);
    }

    // NEW: used by Order service after checkout
    @DeleteMapping("/clearcart/{userid}")
    public void clearCart(@PathVariable String userid) {
        userService.clearCart(userid);
    }
}
