package com.example.auctionseller.service;

import com.example.auctionseller.model.ProductModel;
import com.example.auctionseller.model.ProductOption;
import com.example.auctionseller.model.ReservationDetailsModel;
import com.example.auctionseller.model.frontdto.OptionDto;
import com.example.auctionseller.model.frontdto.ReservationDetails;
import com.example.auctionseller.repository.ProductOptionRepositry;
import com.example.auctionseller.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;

    private final ProductOptionRepositry productOptionRepositry;

    private final ProductService productService;

    public int saveReservation(ReservationDetails reservationDetails){

        Optional<ProductModel> productModel = productService.findProduct(reservationDetails.getProductId());

        List<OptionDto> optionDtoList = reservationDetails.getOptionList();



        if(productModel.isPresent()){

            ReservationDetailsModel reservationDetailsModel =
                    ReservationDetailsModel.builder()
                            .buyerId(reservationDetails.getBuyerId())
                            .quantity(reservationDetails.getQuantity())
                            .productId(productModel.get())
                            .productId(productModel.get()).build();

            int reservationID = reservationRepository.save(reservationDetailsModel).getId();

            for (OptionDto op : optionDtoList
            ) {
                ProductOption productOption
                        = ProductOption
                        .builder()
                        .optionTitle(op.getOptionTitle())
                        .detailedDescription(op.getDetailedDescription())
                        .reservationId(reservationID)
                        .productId(op.getProductId())
                        .build();

                productOptionRepositry.save(productOption);
            }

        }

        return 1;
    }

    public int deleteReservation(){
        return 1;
    }


}
