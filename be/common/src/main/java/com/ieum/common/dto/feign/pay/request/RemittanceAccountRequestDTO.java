package com.ieum.common.dto.feign.pay.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemittanceAccountRequestDTO {

    private Long senderId;
    private String senderName;
    private String receiverName;
    private int amount;
    private Long cardId;
}
