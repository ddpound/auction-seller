package com.example.auctionseller.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Entity
public class CommonReplyOfReplyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @ManyToOne
    @JoinColumn(name = "commonId")
    private CommonReplyModel commonReplyModel;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;
}
