package com.example.auctionseller.repository;

import com.example.auctionseller.model.SubscriberRequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRMRepository extends JpaRepository<SubscriberRequestMessage, Integer> {


    List<SubscriberRequestMessage> findAllByShoppingMallModel_UserId(int shUserId);

    List<SubscriberRequestMessage> findAllByUserIdAndShoppingMallModel_UserId(int userId, int shUserId);

}
