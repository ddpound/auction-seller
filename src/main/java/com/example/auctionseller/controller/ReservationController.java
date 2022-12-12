package com.example.auctionseller.controller;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.model.frontdto.ReservationDetails;
import com.example.auctionseller.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping(value = "seller")
public class ReservationController {

    private final ReservationService reservationService;


    @PostMapping(value = "save-reservation")
    public ResponseEntity<String> saveReservationDetails(@RequestBody ReservationDetails reservationDetails){

        System.out.println("작동테스트");
        System.out.println(reservationDetails);
        System.out.println(reservationDetails.getBuyerId());
        System.out.println(reservationDetails.getOptionList());

        int resultNum = reservationService.saveReservation(reservationDetails);

        if(resultNum ==1 ){
            return new ResponseEntity<String>("save success", HttpStatus.OK);
        }

        return new ResponseEntity<String>("fail save", HttpStatus.BAD_REQUEST);
    }
}
