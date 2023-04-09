package com.example.auctionseller.controller;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.service.ReservationService;
import com.example.auctionseller.service.UserReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Path;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "user")
@RestController
public class UserReservationController {

    private final UserReservationService userReservationService;

    @GetMapping(value = "find-my-reservation/{userId}")
    public List<ReservationDetailsModel> findMyList(@PathVariable int userId){


        return userReservationService.findByBuyer(userId);
    }


}
