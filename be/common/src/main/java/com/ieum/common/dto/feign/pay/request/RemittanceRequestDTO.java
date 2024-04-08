package com.ieum.common.dto.feign.pay.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemittanceRequestDTO {

    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private int amount;
    private Long cardId;
}
