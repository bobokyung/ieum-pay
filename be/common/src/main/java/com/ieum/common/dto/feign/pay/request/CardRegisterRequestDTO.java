package com.ieum.common.dto.feign.pay.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CardRegisterRequestDTO {

    private Long memberId;
    private String cardNumber;
    private String cardNickname;
}

