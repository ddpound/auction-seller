package com.example.auctionseller.model.frontdto;

// 제품 검색시 필요한 DTO

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchingDto {
    private String word;
    private int filter;
}
