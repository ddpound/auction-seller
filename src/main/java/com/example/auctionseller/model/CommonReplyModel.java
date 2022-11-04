package com.example.auctionseller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JoinColumn(name = "commonModelId")
    private CommonModel commonModel;

    // 연관관계의 주인이 누구인지
    @OneToMany(mappedBy = "commonReplyModel" , fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"commonReplyModel"})
    @OrderBy("id desc")
    private List<CommonReplyOfReplyModel> commonReplyOfReplyModels;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;

}
