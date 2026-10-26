package com.Learning.Seller.Service;

import com.Learning.Seller.DTO.Product_DTO;
import com.Learning.Seller.DTO.Stock_Update_DTO;
import com.Learning.Seller.Entity.Product_Entity;
import com.Learning.Seller.Entity.Profile_Entity;
import com.Learning.Seller.Entity.Seller_Entity;
import com.Learning.Seller.Repository.Product_Repository;
import com.Learning.Seller.Repository.Profile_Repository;
import com.Learning.Seller.Repository.Seller_Repository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class Seller_Service {

    @Autowired
     private Seller_Repository sellerRepository;
    @Autowired
     private PasswordEncoder passwordEncoder;

    @Autowired
      private Profile_Repository profileRepository;
    @Autowired
    private Product_Repository productRepository;

    @Autowired
    private  MongoTemplate mongoTemplate;   // used for atomic stock updates


    public Seller_Entity saveSeller(Seller_Entity sellerEntity) {
         if (sellerRepository.findByEmail(sellerEntity
                                            .getEmail())
                                              .isPresent()) {
             throw new IllegalArgumentException("Email already registered");
        }
        sellerEntity.setUserID(null);
        sellerEntity.setPassword(passwordEncoder
                .encode(sellerEntity.getPassword()));
          return sellerRepository.save(sellerEntity);
    }

    public Profile_Entity saveProfile (Profile_Entity profileEntity, String userid) {
          Seller_Entity seller = sellerRepository.findById(userid)
                  .orElseThrow(() -> new NoSuchElementException("Seller not found:" + userid));
        profileEntity.setSellerEntity(seller);
        return profileRepository.save(profileEntity);
    }



    public Product_Entity addproduct(String userid, Product_Entity productEntity) {

        if (!sellerRepository.existsById(userid)) {

            throw new NoSuchElementException("Seller not found: " + userid);
        }

            if (productEntity.getPrice() <= 0&&productEntity.getQuantity() < 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        productEntity.setProductId(null);// it will set auto by mongo
        productEntity.setDate(LocalDate.now());
        productEntity.setUserid(userid);
        return productRepository.save(productEntity); // other parameter is send by seller through json
    }

    // one product (used by User service for cart and Order service for checkout)
    public Product_DTO getProductById(String productId) {
        Product_Entity product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));
        return toDto(product);
    }

    public List<Product_DTO> showproduct(String show) {
         List<Product_DTO> result = new ArrayList<>();
        if (show.equalsIgnoreCase("show")) {
            for (Product_Entity p : productRepository.findAll()) {
                result.add(toDto(p));
            }
        }
        return result;
    }

    public List<Product_DTO> getProductsBySeller(String userid) {
        List<Product_DTO> result = new ArrayList<>();
        for (Product_Entity p : productRepository.findByUserid(userid)) {
            result.add(toDto(p));
        }
        return result;
    }

    // ---------------- Stock (called by Order service) ----------------

    // Atomic: "reduce only if quantity >= requested" happens in ONE database
    // operation, so two people can never buy the last item at the same time.
    public void reduceStock(Stock_Update_DTO dto) {
        validate(dto);
        Query query = new Query(Criteria.where("productId").is(dto.getProductId())
                .and("quantity").gte(dto.getQuantity()));
        Update update = new Update().inc("quantity", -dto.getQuantity());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product_Entity.class);

        if (result.getModifiedCount() == 0) {
            if (!productRepository.existsById(dto.getProductId())) {
                throw new NoSuchElementException("Product not found: " + dto.getProductId());
            }
            throw new IllegalArgumentException("Not enough stock  for product: " + dto.getProductId());
        }
    }

    // used when an order is cancelled (or checkout fails half way)
    public void restoreStock(Stock_Update_DTO dto) {
        validate(dto);
        Query query = new Query(Criteria.where("productId").is(dto.getProductId()));
        Update update = new Update().inc("quantity", dto.getQuantity());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product_Entity.class);
        if (result.getMatchedCount() == 0) {
            throw new NoSuchElementException("Product not found: " + dto.getProductId());
        }
    }

    // ---------------- helpers ----------------

    private void validate(Stock_Update_DTO dto) {
        if (dto.getProductId() == null || dto.getProductId().isBlank()) {
            throw new IllegalArgumentException("productId is required");
        }
        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private Product_DTO toDto(Product_Entity p) {
        Product_DTO dto = new Product_DTO();
        dto.setProductId(p.getProductId());
        dto.setProductName(p.getProductName());
        dto.setProductImage(p.getProductImage());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setQuantity(p.getQuantity());
        return dto;
    }
}
