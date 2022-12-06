package com.example.auctionseller.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
    @ManyToOne
    private ProductModel productId;

    private int quantity;

    private int sellerId;

    // 구매자 아이디
    private int buyerId;

    @CreationTimestamp
    private Timestamp createDate;

}
