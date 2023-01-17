package com.example.auctionseller.model.frontdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationStatusDto {
    private List<Integer> reservationList;
    private int status;
}
