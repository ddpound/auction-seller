package com.example.auctionseller.controller;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.model.frontdto.ReservationDetails;
import com.example.auctionseller.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping(value = "seller")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 쇼핑몰 id ->  userId ->
     * */
    @GetMapping("find-all-reservation/{shoppingMallId}")
    public ResponseEntity<List> findAllReservation(@PathVariable(value = "shoppingMallId")int shoppingMallId){
        return new ResponseEntity<>(reservationService.findAllReservationByShoppingMallId(shoppingMallId),HttpStatus.OK);
    }

    @GetMapping("find-reservation/{reservationId}")
    public ResponseEntity<String> findReservation(@PathVariable(value = "reservationId")int reservationID){

        return new ResponseEntity<>("",HttpStatus.OK);
    }


    @PostMapping(value = "save-reservation")
    public ResponseEntity<String> saveReservationDetails(@RequestBody ReservationDetails reservationDetails){

        int resultNum = reservationService.saveReservation(reservationDetails);

        if(resultNum ==1 ){
            return new ResponseEntity<String>("save success", HttpStatus.OK);
        }

        return new ResponseEntity<String>("fail save", HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "change-reservation/{status}")
    public ResponseEntity<String> changeReservationStatus(@RequestBody ReservationDetailsModel reservationDetailsModel,
                                                          @PathVariable int status){
        System.out.println(reservationDetailsModel.getBuyerId());
        System.out.println(reservationDetailsModel.getBuyerNickName());
        System.out.println(reservationDetailsModel.getShoppingMallId());
        System.out.println(reservationDetailsModel);
        System.out.println(status);



        return new ResponseEntity<String>("save success", HttpStatus.OK);
    }


}
