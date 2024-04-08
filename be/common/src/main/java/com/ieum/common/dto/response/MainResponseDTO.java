package com.ieum.common.dto.response;

import com.ieum.common.dto.feign.pay.dto.CardDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainResponseDTO {
    int paymentAmount;
    int totalDonation;
    boolean isLinked;
    List<CardDTO> cardList;
}

