package com.Learning.Seller.Controller;

import com.Learning.Seller.DTO.Product_DTO;
import com.Learning.Seller.DTO.Stock_Update_DTO;
import com.Learning.Seller.Entity.Product_Entity;
import com.Learning.Seller.Entity.Profile_Entity;
import com.Learning.Seller.Entity.Seller_Entity;
import com.Learning.Seller.Entity.Seller_Login_Entity;
import com.Learning.Seller.Service.Seller_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class Seller_controller {

    @Autowired
    Seller_Service service;
    @Autowired
    AuthenticationManager authenticationManager;
    // removed the stray @Autowired that was sitting above @PostMapping("/signup")

    @PostMapping("/signup")
    public Seller_Entity signup(@RequestBody Seller_Entity sellerEntity) {
        return service.saveSeller(sellerEntity);
    }

    @PostMapping("/login")
    public String authenticate(@RequestBody Seller_Login_Entity login) {
        // wrong email/password throws an exception -> handled as 401 by GlobalExceptionHandler
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        return authenticate.isAuthenticated() ? "Login successful" : "Login failed";
    }

    @PostMapping("/saveprofile/{userid}")
    public Profile_Entity saveprofile(@PathVariable String userid,
                                      @RequestBody Profile_Entity profileEntity) {
        return service.saveProfile(profileEntity, userid);
    }

    @PostMapping("/addproduct/{userid}")
    public Product_Entity addproduct(@PathVariable String userid,
                                     @RequestBody Product_Entity productEntity) {
        return service.addproduct(userid, productEntity);
    }

    // All products of one seller
    @GetMapping("/myproducts/{userid}")
    public List<Product_DTO> myProducts(@PathVariable String userid) {
        return service.getProductsBySeller(userid);
    }

    // Single product. This ONE endpoint replaces the old
    // /seller/product/{id} and /seller/addproduct/{productid}
    @GetMapping("/product/{id}")
    public Product_DTO getProduct(@PathVariable String id) {
        return service.getProductById(id);
    }

    @GetMapping("/show/{showlist}")
    public List<Product_DTO> showall(@PathVariable String showlist) {
        return service.showproduct(showlist);
    }

    // ---- used by Order service ----
    @PutMapping("/reduce-stock")
    public void reduceStock(@RequestBody Stock_Update_DTO dto) {
        service.reduceStock(dto);
    }

    @PutMapping("/restore-stock")
    public void restoreStock(@RequestBody Stock_Update_DTO dto) {
        service.restoreStock(dto);
    }
}
