package com.example.auctionseller.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ShoppingMallModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String shoppingMallName;

    private String shoppingMallExplanation;

    @Column(unique = true, nullable = false)
    private int userId;

    @Column(unique = true , nullable = false)
    @JsonIgnore
    private String username;

    // 사진 경로를 남길듯
    @Column(length = 1000)
    private String thumbnailUrlPath;

    // 삭제를 위한 사진경로
    @Column(length = 1000)
    private String thumbnailFilePath;

    /**
     * 이미지파일이 저장된 고유 폴더 경로
     * */
    @Column(length = 500)
    private String filefolderPath;

    @CreationTimestamp
    private Timestamp createShoppingMall;

    @UpdateTimestamp
    private Timestamp modifyShoppingMall;


}
