package com.example.auctionseller.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;


/**
 * 구매, 예약 내역
 * */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReservationDetailsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // 제품 아이디
    private int productId;

    // 유저 아이디
    private int userId;


    @CreationTimestamp
    private Timestamp createDate;

}
