package com.example.auctionseller.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SubscriberRequestMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int userId;

    @ManyToOne
    private ShoppingMallModel shoppingMallModel;

    private String username;

    private String message;


}
