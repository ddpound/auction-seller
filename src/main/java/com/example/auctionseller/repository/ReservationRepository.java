package com.example.auctionseller.repository;

import com.example.auctionseller.model.ReservationDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationDetailsModel, Integer> {
}
