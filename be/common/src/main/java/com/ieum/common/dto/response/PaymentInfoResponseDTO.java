package com.ieum.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfoResponseDTO {

    private Long storeId;
    private int price;
    private String cardNickname;
    private String storeName;
    private String facilityName;
    private int paymoneyAmount;
    private int chargeAmount;
    private int donationMoney;

}
