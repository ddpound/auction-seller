package com.example.auctionseller.service;


import com.example.auctionseller.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class BillService {

    private final BillRepository billRepository;




}
