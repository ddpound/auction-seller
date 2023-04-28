package com.example.auctionseller.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private ShoppingMallModel shoppingMallModel;

    private int userId;

    private String username;

    // 블랙리스트 유무
    private boolean black;

}
