package com.Learning.User.Service;

import com.Learning.User.DTO.Add_Product;
import com.Learning.User.DTO.Product_Response;
import com.Learning.User.DTO.Return_type_dto;
import com.Learning.User.DTO.User_Login_DTO;
import com.Learning.User.Entity.Cart_Entity;
import com.Learning.User.Entity.UserEntity;
import com.Learning.User.FeignCient;
import com.Learning.User.Repository.Cart_Repository;
import com.Learning.User.Repository.User_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class User_service {

    @Autowired
    User_Repository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    FeignCient feignCient;
    @Autowired
    Cart_Repository cartRepository;

    // ---------------- Account ----------------

    public UserEntity signup(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        userEntity.setUserId(null);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public UserEntity login(User_Login_DTO dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return user;   // password is hidden by @JsonProperty(WRITE_ONLY)
    }

    //Products

    public List<Product_Response> show(String s) {
        return feignCient.show(s);
    }

    // Cart

    public Cart_Entity addproduct(Add_Product req) {
        if (req.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!userRepository.existsById(req.getUserID())) {
            throw new NoSuchElementException("User not found: " + req.getUserID());
        }

        // 1. get the latest price and stock from Seller service
        Product_Response product = feignCient.getProduct(req.getProductID());

        // 2. find the users cart, or create a new one
        Cart_Entity cart = cartRepository.findByUserId(req.getUserID()).orElseGet(() -> {
            Cart_Entity c = new Cart_Entity(); // if cart is not fund then create new cart
            c.setUserId(req.getUserID()); // Set userid in that cart
            return c;   // it will send ready  to return cart
        });

        // 3. is this product already in the cart? then just increase quantity
        Return_type_dto item = null;
        for (Return_type_dto i : cart.getItems()) {
            if (i.getProductid().equals(req.getProductID())) {
                item = i;  // Item = an object of return type DTO
                break;
            }
        }
        int newQuantity = req.getQuantity() + (item == null ? 0 : item.getQuantity());

        // 4. check stock
        if (newQuantity > product.getQuantity()) {
            throw new IllegalArgumentException("Only " + product.getQuantity()
                    + " item(s) of '" + product.getProductName() + "' available");
        }

        if (item == null) {
            item = new Return_type_dto();
            item.setProductid(req.getProductID());
            cart.getItems().add(item);
        }
        item.setProductName(product.getProductName());
        item.setPrice(product.getPrice());
        item.setQuantity(newQuantity);
        item.setTotal_price(product.getPrice() * newQuantity);

        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    public Cart_Entity getMyCart(String userid) {
        return cartRepository.findByUserId(userid)
                .orElseThrow(() -> new NoSuchElementException("Cart is empty for user: " + userid));
    }

    public Cart_Entity removeItem(String userid, String productId) {
        Cart_Entity cart = getMyCart(userid);
        boolean removed = cart.getItems().removeIf(i -> i.getProductid().equals(productId));
        if (!removed) {
            throw new NoSuchElementException("Product not in cart: " + productId);
        }
        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    // called by Order service after an order is placed
    public void clearCart(String userid) {
        cartRepository.findByUserId(userid).ifPresent(cartRepository::delete);
    }

    private void recalculateTotal(Cart_Entity cart) {
        double total = 0;
        for (Return_type_dto i : cart.getItems()) {
            total += i.getTotal_price();
        }
        cart.setTotal_cart_price(total);
    }
}
