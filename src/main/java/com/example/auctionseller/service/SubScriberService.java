package com.example.auctionseller.service;

import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.model.SubscriberRequestMessage;
import com.example.auctionseller.model.frontdto.SubscriberMessageDTO;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.auctionseller.repository.SubscriberRMRepository;
import com.example.auctionseller.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class SubScriberService {

    private final SubscriberRepository subscriberRepository;

    private final SubscriberRMRepository subscriberRMRepository;

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    @Transactional
    public int subscriberMessageSave(SubscriberRequestMessage subScriberRequestMessage){

        Optional<ShoppingMallModel> shoppingMallModel = shoppingMallModelRepositry.findById(subScriberRequestMessage.getShoppingMallModel().getId());

        if(shoppingMallModel.isPresent()){
            subScriberRequestMessage.setShoppingMallModel(shoppingMallModel.get());
            return 1;
        }

        return -1;
    }




    /**
     * @param shUserId 쇼핑몰 관리자 유저 아이디
     * */
    @Transactional(readOnly = true)
    public List<SubscriberMessageDTO> findAllSubsciberRequestMessage(int shUserId,
                                                                         HttpServletRequest httpServletRequest){
        List<SubscriberMessageDTO> returnSubscriberMessage = new ArrayList<>();

        subscriberRMRepository.findAllByShoppingMallModel_UserId(shUserId)
                .stream()
                .map(subscriberRequestMessage -> {

                    return returnSubscriberMessage
                            .add(SubscriberMessageDTO
                                    .builder()
                                    .message(subscriberRequestMessage.getMessage())
                                    .username(subscriberRequestMessage.getUsername())
                                    .userId(subscriberRequestMessage.getUserId())
                                    .shppingMallUserId(subscriberRequestMessage.getShoppingMallModel().getUserId())
                                    .shoppingMallName(subscriberRequestMessage.getShoppingMallModel().getShoppingMallName())
                                    .build());
                });


        return returnSubscriberMessage;
    }



}
