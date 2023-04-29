package com.example.auctionseller.repository;

import com.example.auctionseller.model.ProductModel;
import com.example.auctionseller.model.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Integer> {


    void deleteAllByProductId(int productId);
}
