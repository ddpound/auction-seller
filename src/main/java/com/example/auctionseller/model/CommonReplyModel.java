package com.example.auctionseller.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CommonReplyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 1000)
    private String content;

    private int userId;

    private String nickName;

    @Column(length = 400)
    private String userPicture;

    // Json Ignore도 작동하지 않으며 원인도 찾아내지못해 현재 이렇게 수정됨
    private int commonModelId;

    /**
     * 무한참조 방지를 위해 그리고 편한
     * select를 위해서 양방향이 아닌
     * 단방향을 채택하기로했습니다.
     *
     * */
    @OneToMany(mappedBy = "commonReplyId", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"commonReplyModel"})
    @JsonManagedReference
    List<CommonReplyOfReplyModel> commonReplyOfReplyModels;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;

}
