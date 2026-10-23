package com.Learning.Seller.SecurityConfigurations;

import com.Learning.Seller.Entity.Seller_Entity;
import com.Learning.Seller.Repository.Seller_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    Seller_Repository sellerRepository;

    public Seller_Entity getusername(String email){
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Seller_Entity sellerEntity = sellerRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return User.builder()
                .username(sellerEntity.getEmail())
                .password(sellerEntity.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }


}

