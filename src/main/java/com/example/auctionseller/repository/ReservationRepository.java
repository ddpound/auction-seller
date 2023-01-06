package com.example.auctionseller.repository;

import com.example.auctionseller.model.ReservationDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationDetailsModel, Integer> {

    List<ReservationDetailsModel> findAllByShoppingMallId(int ShoppingMallId);


    List<ReservationDetailsModel> findAllByShoppingMallIdAndBuyerNickNameLike(int ShoppingMallId, String NickName);


    List<ReservationDetailsModel> findAllByShoppingMallIdAndProductId(int ShoppingMallId, int ProductId);

}
