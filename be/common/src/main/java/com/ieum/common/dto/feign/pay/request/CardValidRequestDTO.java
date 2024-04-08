package com.ieum.common.dto.feign.pay.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardValidRequestDTO {

    private Long memberId;
    private Long registeredCardId;
}
