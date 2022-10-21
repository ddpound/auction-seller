package com.example.auctionseller.repository;


import com.example.auctionseller.model.ShoppingMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingMallModelRepositry extends JpaRepository<ShoppingMallModel,Integer> {

    ShoppingMallModel findByUsername(String username);

    // 이름 중복 검사
    ShoppingMallModel findByShoppingMallName(String shoppingMallName);

    void deleteByUsername(String username);

}
