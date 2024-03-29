package com.example.auctionseller.controller;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.model.frontdto.ReservationDetails;
import com.example.auctionseller.model.frontdto.ReservationStatusDto;
import com.example.auctionseller.model.frontdto.SearchingDto;
import com.example.auctionseller.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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


    /**
     * 유저가 구매내역을 저장
     * */
    @PostMapping(value = "save-reservation")
    public ResponseEntity<String> saveReservationDetails(@RequestBody ReservationDetails reservationDetails){

        int resultNum = reservationService.saveReservation(reservationDetails);

        if(resultNum ==1 ){
            return new ResponseEntity<String>("save success", HttpStatus.OK);
        }

        return new ResponseEntity<String>("fail save", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "delete-reservation/{reservationId}")
    public ResponseEntity<String> deleteReservationDetails(@PathVariable int reservationId){

        int resultNum = reservationService.deleteReservation(reservationId);

        if(resultNum ==1 ){
            return new ResponseEntity<String>("delete success", HttpStatus.OK);
        }

        if(resultNum == -3){
            return new ResponseEntity<String>("not found reservation", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("fail delete", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "delete-reservation-list")
    public ResponseEntity<String> deleteReservationDetailList(@RequestBody ReservationStatusDto reservationList){

        int resultNum = reservationService.deleteReservationList(reservationList.getReservationList());

        if(resultNum ==1 ){
            return new ResponseEntity<String>("delete reservationList success", HttpStatus.OK);
        }

        if(resultNum == -3){
            return new ResponseEntity<String>("not found reservation", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("fail delete", HttpStatus.BAD_REQUEST);
    }



    @PostMapping(value = "change-reservation/{status}")
    public ResponseEntity<String> changeReservationStatus(@RequestBody ReservationDetailsModel reservationDetailsModel,
                                                          @PathVariable int status){

        int resultNum = reservationService.changeStatusReservation(reservationDetailsModel,status);

        if(resultNum ==1 ){
            return new ResponseEntity<String>("save success", HttpStatus.OK);
        }else if(resultNum == -1){
            return new ResponseEntity<String>("Sorry fail change not found reservationDetailModel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else{
            return new ResponseEntity<String>("Sorry fail change reservation status", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "change-reservation-list")
    public ResponseEntity<String> changeReservationStatusList(@RequestBody ReservationStatusDto reservationList){

        int resultNum = reservationService.changeStatusReservationList(reservationList.getReservationList(), reservationList.getStatus());

        if(resultNum == 1){
            return new ResponseEntity<String>("status change success", HttpStatus.OK);
        }else{

            return new ResponseEntity<String>("fail change status", HttpStatus.BAD_REQUEST);
        }
    }

    // 원하는 검색어와 필터를 가져오기
    /**
     * 1 은 판매제품
     * 2 는 구매자 검색
     * 3 은 날짜
     * 4 는 상태
     * */
    @GetMapping(value = "search")
    public ResponseEntity<List> searchReservation(@RequestParam int filter,
                                                  @RequestParam String word,
                                                  @RequestParam Timestamp start,
                                                  @RequestParam Timestamp end,
                                                  @RequestParam String searchProductState,
                                                  @RequestParam int shoppingMallId){

        if(filter ==1 && word.length() > 0 ){
            return new ResponseEntity<>(reservationService.findSearchProductName(shoppingMallId,word), HttpStatus.OK);
        }

        if(filter ==2 && word.length() > 0 ){
            return new ResponseEntity<>(reservationService.findSearchNickName(shoppingMallId,word), HttpStatus.OK);
        }

        if(filter == 3){
            return new ResponseEntity<>(reservationService.findSearchCreateDateBetween(start,end,shoppingMallId), HttpStatus.OK);
        }
        if(filter == 4){
            return new ResponseEntity<>(reservationService.findSearchState(shoppingMallId,searchProductState), HttpStatus.OK);
        }
        // 필터 없거나 word가 없으면 그냥 전체를 보내주기
        return new ResponseEntity<>(reservationService.findAllReservationByShoppingMallId(shoppingMallId), HttpStatus.OK);
    }


}
