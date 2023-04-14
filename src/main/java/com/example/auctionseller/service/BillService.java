package com.example.auctionseller.service;


import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.model.enums.BillStatus;
import com.example.auctionseller.repository.BillRepository;
import com.example.auctionseller.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class BillService {

    private final ReservationRepository reservationRepository;

    private final BillRepository billRepository;



    /**
     * 청구서 요청
     * */
    @Transactional
    public String requestBill(int id){

        Optional<ReservationDetailsModel> findReservationModel = reservationRepository.findById(id);

        findReservationModel.ifPresent(reservationDetailsModel -> reservationDetailsModel.setBillStatus(BillStatus.입금요청));


        return "";
    }


}
