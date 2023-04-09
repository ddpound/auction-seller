package com.example.auctionseller.service;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<ReservationDetailsModel> findByBuyer(int buyerId){

        return reservationRepository.findAllByBuyerId(buyerId);
    }


}
