package com.example.auctionseller.model;


import com.example.auctionseller.model.enums.BillStatus;
import com.example.auctionseller.model.enums.ReservationStatus;
import com.example.auctionseller.model.frontdto.OptionDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    // 제품 안에 이미 쇼핑몰 -> 판매자 아이디가 있음
    //private int sellerId;

    // 구매자 아이디
    private int buyerId;

    private String buyerNickName;

    private int shoppingMallId;

    private String address;
    
    private ReservationStatus reservationStatus;

    // 계산서 요청
    private BillStatus billStatus;

    @OneToMany(mappedBy = "reservationId" ,fetch = FetchType.LAZY)
    private List<ReservationOption> options;

    @CreationTimestamp
    private Timestamp createDate;

}
