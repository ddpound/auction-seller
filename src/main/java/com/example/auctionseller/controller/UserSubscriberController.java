package com.example.auctionseller.controller;

import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.model.SubscriberRequestMessage;
import com.example.auctionseller.service.SubScriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "user")
@RestController
public class UserSubscriberController {

    private final SubScriberService subScriberService;

    @PostMapping(value = "sub-message-request")
    public ResponseEntity<String> findMyList(@RequestBody SubscriberRequestMessage subScriberRequestMessage){


        int result = subScriberService.subscriberMessageSave(subScriberRequestMessage);

        if(result ==1){
            return new ResponseEntity<>("success subscriber request message", HttpStatus.OK);
        }


        return new ResponseEntity<>("sorry fail save, server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
