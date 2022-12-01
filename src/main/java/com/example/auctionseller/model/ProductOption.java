package com.example.auctionseller.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 제품 옵션을 붙이는 곳
 * */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String optionTitle;

    // 옵션의 자세한 설명
    private String detailedDescription;

    private int productId;


}
