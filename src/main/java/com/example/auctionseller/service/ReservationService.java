package com.example.auctionseller.service;

import com.example.auctionseller.repository.ReservationRepository;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;

    public int saveReservation(){
        return 1;
    }

    public int deleteReservation(){
        return 1;
    }


}
