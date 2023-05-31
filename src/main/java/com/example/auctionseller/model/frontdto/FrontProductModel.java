package com.example.auctionseller.model.frontdto;


import com.example.auctionseller.model.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * 프론트단에 보내줄 객체
 * */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FrontProductModel {

    private int id;

    private FrontShppingMallDto frontShppingMallDto;

    // 제품이름
    private String productName;

    // 제품 가격
    private int productPrice;

    // 퀄리티와 다른 퀀티티, 즉 수량을 말함
    private int productQuantity;

    private String content;

    private String pictureFilePath;

    private String pictureUrlPath;

    private String filefolderPath;

    List<ProductOption> productOptionList;

    private Timestamp createDate;

    private Timestamp modifyDate;
}
