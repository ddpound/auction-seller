package com.example.auctionseller.model;

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
@Entity
public class CommonReplyOfReplyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int userId;

    private String nickName;

    @ManyToOne
    @JoinColumn(name = "commonReplyModelId")
    private CommonReplyModel commonReplyModel;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;
}
