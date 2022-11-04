package com.example.auctionseller.service;

import com.example.auctionseller.model.CommonModel;
import com.example.auctionseller.model.ProductModel;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.CommonModelRepository;
import com.example.auctionseller.repository.ProductModelRepository;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallServiceAll {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final CommonModelRepository commonModelRepository;

    private final ProductModelRepository productModelRepository;

    @Transactional(readOnly = true)
    public Optional<ShoppingMallModel> findShoppingMall(int shoppingMall){
        return shoppingMallModelRepositry.findById(shoppingMall);
    }

    @Transactional(readOnly = true)
    public List<ProductModel> findAllProduct(int shoppingmallId){

        ArrayList<ProductModel> listProductModel = new ArrayList<>();


        return productModelRepository.findAllByShoppingMall(shoppingMallModelRepositry.findById(shoppingmallId));
    }

    @Transactional(readOnly = true)
    public ProductModel findProductModel(int productId){
        Optional<ProductModel> productModel =  productModelRepository.findById(productId);

        // 만약 결과값이 없다면 null을 반환
        return productModel.orElse(null);
    }

    /**
     * 판매자가 작성한 글의 리스트를 가져와주는 함수
     * */
    @Transactional(readOnly = true)
    public List<CommonModel> findAllCommonModel(Integer shoppingMallId){

        Optional<ShoppingMallModel>shoppingMallModel = shoppingMallModelRepositry.findById(shoppingMallId);

        return commonModelRepository.findAllByShoppingMall(shoppingMallModel.get());
    }

    /**
     * 판매자가 작성한 글을 가져와주는 함수
     * */
    @Transactional(readOnly = true)
    public CommonModel findCommonModel(int boardId){
        Optional<CommonModel> commonModel =  commonModelRepository.findById(boardId);
        commonModel.ifPresent(model -> model.getShoppingMall().setUsername(""));

        // 만약 결과값이 없다면 null을 반환
        return commonModel.orElse(null);
    }
}
