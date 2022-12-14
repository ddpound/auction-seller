package com.example.auctionseller.service;

import com.example.auctionseller.model.*;
import com.example.auctionseller.model.enums.ReservationStatus;
import com.example.auctionseller.model.frontdto.OptionDto;
import com.example.auctionseller.model.frontdto.ReservationDetails;
import com.example.auctionseller.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;

    private final ReservationOptionRepository reservationOptionRepository;

    private final ProductModelRepository productModelRepository;

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<ReservationDetailsModel> findAllReservationByShoppingMallId(int shoppingMallId){
        return reservationRepository.findAllByShoppingMallId(shoppingMallId);
    }


    @Transactional
    public int saveReservation(ReservationDetails reservationDetails){

        Optional<ProductModel> productModel = productService.findProduct(reservationDetails.getProductId());

        List<OptionDto> optionDtoList = reservationDetails.getOptionList();

        if(productModel.isPresent()){

            ReservationDetailsModel reservationDetailsModel =
                    ReservationDetailsModel.builder()
                            .buyerId(reservationDetails.getBuyerId())
                            .quantity(reservationDetails.getQuantity())
                            .shoppingMallId(reservationDetails.getShoppingMallId())
                            .buyerNickName(reservationDetails.getBuyerNickName())
                            .address(reservationDetails.getAddress())
                            .productId(productModel.get())
                            .productId(productModel.get())
                            .reservationStatus(ReservationStatus.??????)
                            .build();

            int reservationID = reservationRepository.save(reservationDetailsModel).getId();

            System.out.println(optionDtoList.size());

            for (OptionDto op : optionDtoList
            ) {
                ReservationOption reservationOption
                        = ReservationOption
                        .builder()
                        .optionTitle(op.getOptionTitle())
                        .detailedDescription(op.getDetailedDescription())
                        .reservationId(reservationID)
                        .productId(op.getProductId())
                        .build();
                reservationOptionRepository.save(reservationOption);
            }

        }

        return 1;
    }

    @Transactional
    public int deleteReservation(ReservationDetailsModel reservationDetailsModel){

        Optional<ReservationDetailsModel> findReservationDetailsModel =
                reservationRepository.findById(reservationDetailsModel.getId());

        if(findReservationDetailsModel.isPresent()){
            reservationRepository.delete(reservationDetailsModel);
            return 1;
        }else {
            return -1;
        }
    }

    @Transactional
    public int changeStatusReservation(ReservationDetailsModel reservationDetailsModel,
                                       int status){

        Optional<ReservationDetailsModel> findReservationDetailsModel =
                reservationRepository.findById(reservationDetailsModel.getId());

        if(findReservationDetailsModel.isPresent()){
            findReservationDetailsModel.get().setReservationStatus(ReservationStatus.values()[status]);
            return 1;
        }else {
            return -1;
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailsModel> findSearchNickName(int shoppingMallId,String word){
        return reservationRepository.findAllByShoppingMallIdAndBuyerNickNameLike(shoppingMallId,"%"+word+"%");
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailsModel> findSearchProductName(int shoppingMallId,String word){
        return reservationRepository.findAllByShoppingMallIdAndProductId_ProductNameLike(shoppingMallId, "%"+word+"%");
    }


}
