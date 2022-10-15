package com.example.auctionseller.repository;


import com.example.auctionseller.model.BoardCategory;
import com.example.auctionseller.model.ShoppingMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Integer> {



    List<BoardCategory> findAllByShoppingMall(ShoppingMallModel shoppingMallModel);

    BoardCategory findByShoppingMall(ShoppingMallModel shoppingMallModel);

    BoardCategory findByCategoryName(String categoryName);
}
