package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.CommonReplyModel;
import com.example.auctionseller.model.CommonReplyOfReplyModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

public class FrontCommonReplyOfReply {

    private int id;

    private int userId;

    private String content;

    private String nickName;

    private String userPicture;

    /**
     * 보드 아이디
     * */
    private int commonModelId;
    /**
     * 대댓글
     * */
    private int commonReplyModelId;

    private FrontCommonReplyModel commonReplyModel;
    private Timestamp createDate;

    private Timestamp modifyDate;

    public FrontCommonReplyOfReply(CommonReplyOfReplyModel commonReplyOfReplyModel){
        this.id = commonReplyOfReplyModel.getId();
        this.content = commonReplyOfReplyModel.getContent();
        this.nickName = commonReplyOfReplyModel.getNickName();
        this.userPicture = commonReplyOfReplyModel.getUserPicture();
        this.commonModelId = commonReplyOfReplyModel.getCommonModelId();
        this.commonReplyModelId = commonReplyOfReplyModel.getCommonReplyModel().getCommonModelId();
        this.createDate = commonReplyOfReplyModel.getCreateDate();
        this.modifyDate = commonReplyOfReplyModel.getModifyDate();
    }
}
