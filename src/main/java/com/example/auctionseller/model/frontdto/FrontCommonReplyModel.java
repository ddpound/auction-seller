package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.CommonReplyModel;
import com.example.auctionseller.model.CommonReplyOfReplyModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontCommonReplyModel {

    private int id;

    private String content;

    private int userId;

    private String nickName;

    private String userPicture;

    private int commonModelId;

    private List<CommonReplyOfReplyModel> commonReplyOfReplyModels;
    private Timestamp createDate;

    private Timestamp modifyDate;


    public FrontCommonReplyModel(CommonReplyModel commonReplyModel){
        this.id = commonReplyModel.getId();
        this.content = commonReplyModel.getContent();
        this.nickName = commonReplyModel.getNickName();
        this.userPicture = commonReplyModel.getUserPicture();
        this.createDate = commonReplyModel.getCreateDate();
        this.modifyDate = commonReplyModel.getModifyDate();
    }


}
