package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.ShoppingMallModel;

import java.sql.Timestamp;

public class FrontShppingMallDto {

    private int id;

    private int userId;
    private String shoppingMallName;
    private String shoppingMallExplanation;
    private String thumbnailUrlPath;
    private String thumbnailFilePath;
    private String filefolderPath;
    private Timestamp createShoppingMall;
    private Timestamp modifyShoppingMall;

    public FrontShppingMallDto(ShoppingMallModel shoppingMallModel){
        this.id = shoppingMallModel.getId();
        this.userId = shoppingMallModel.getUserId();
        this.shoppingMallName = shoppingMallModel.getShoppingMallName();
        this.shoppingMallExplanation = shoppingMallModel.getShoppingMallExplanation();
        this.thumbnailUrlPath = shoppingMallModel.getThumbnailUrlPath();
        this.thumbnailFilePath = shoppingMallModel.getThumbnailFilePath();
        this.filefolderPath = shoppingMallModel.getFilefolderPath();
        this.createShoppingMall = shoppingMallModel.getCreateShoppingMall();
        this.modifyShoppingMall = shoppingMallModel.getModifyShoppingMall();
    }

}
