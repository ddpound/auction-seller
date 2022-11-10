package com.example.auctionseller.model.frontdto;

import com.example.auctionseller.model.CommonReplyModel;
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

    private Timestamp createDate;

    private Timestamp modifyDate;
}
