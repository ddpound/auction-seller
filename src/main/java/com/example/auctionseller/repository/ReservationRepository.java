package com.example.auctionseller.repository;

import com.example.auctionseller.model.ProductModel;
import com.example.auctionseller.model.ReservationDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationDetailsModel, Integer> {

    List<ReservationDetailsModel> findAllByShoppingMallId(int ShoppingMallId);


    List<ReservationDetailsModel> findAllByShoppingMallIdAndBuyerNickNameLike(int ShoppingMallId, String NickName);


    List<ReservationDetailsModel> findAllByShoppingMallIdAndProductId_ProductNameLike(int shoppingMallId, String productName);

    List<ReservationDetailsModel> findAllByCreateDateBetween(Timestamp startTime,Timestamp endTime);
}
