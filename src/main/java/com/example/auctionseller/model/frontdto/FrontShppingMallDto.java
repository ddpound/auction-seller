package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.ShoppingMallModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrontShppingMallDto {

    private int id;

    private int userId;
    private String nickname;
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
        this.nickname = shoppingMallModel.getNickname();
        this.shoppingMallName = shoppingMallModel.getShoppingMallName();
        this.shoppingMallExplanation = shoppingMallModel.getShoppingMallExplanation();
        this.thumbnailUrlPath = shoppingMallModel.getThumbnailUrlPath();
        this.thumbnailFilePath = shoppingMallModel.getThumbnailFilePath();
        this.filefolderPath = shoppingMallModel.getFilefolderPath();
        this.createShoppingMall = shoppingMallModel.getCreateShoppingMall();
        this.modifyShoppingMall = shoppingMallModel.getModifyShoppingMall();
    }

}
