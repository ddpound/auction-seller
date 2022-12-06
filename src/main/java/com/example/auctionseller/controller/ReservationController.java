package com.example.auctionseller.controller;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Log4j2
@RestController
public class ReservationController {

    private final ReservationService reservationService;


    @PostMapping
    public ResponseEntity saveReservationDetails(@RequestBody ReservationDetailsModel reservationDetailsModel){

        return null;
    }
}
