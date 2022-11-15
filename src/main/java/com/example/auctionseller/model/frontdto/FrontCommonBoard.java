package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.BoardCategory;
import com.example.auctionseller.model.CommonModel;
import com.example.auctionseller.model.CommonReplyModel;
import com.example.auctionseller.model.ShoppingMallModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


public class FrontCommonBoard {

    private int id;
    private String title;
    private FrontShppingMallDto shoppingMall;
    private String Content;
    private String pictureFilePath;
    private String pictureUrlPath;
    private String filefolderPath;
    private List<FrontCommonReplyModel> frontCommonReplyModel;
    private BoardCategory boardCategory;
    private Timestamp createDate;
    private Timestamp modifyDate;

    public FrontCommonBoard(CommonModel commonModel,
                            FrontShppingMallDto shoppingMall,
                            List<FrontCommonReplyModel> frontCommonReplyModel) {

        this.id = commonModel.getId();
        this.title = commonModel.getTitle();
        this.shoppingMall = shoppingMall;
        Content = commonModel.getContent();
        this.pictureFilePath = commonModel.getPictureFilePath();
        this.pictureUrlPath = commonModel.getPictureUrlPath();
        this.filefolderPath = commonModel.getFilefolderPath();
        this.frontCommonReplyModel = frontCommonReplyModel;
        this.boardCategory = commonModel.getBoardCategory();
        this.createDate = commonModel.getCreateDate();
        this.modifyDate = commonModel.getModifyDate();



    }
}
