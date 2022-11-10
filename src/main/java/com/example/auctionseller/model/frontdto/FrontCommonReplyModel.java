package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.CommonReplyOfReplyModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

public class FrontCommonReplyModel {

    private int id;

    private String content;

    private int userId;

    private String nickName;

    private String userPicture;


    private int commonModelId;

    List<FrontCommonReplyOfReply> commonReplyOfReplyModels;

    private Timestamp createDate;

    private Timestamp modifyDate;


}
