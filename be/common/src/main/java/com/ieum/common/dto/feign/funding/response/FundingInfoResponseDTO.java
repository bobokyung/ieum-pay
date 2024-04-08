package com.ieum.common.dto.feign.funding.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundingInfoResponseDTO {

    private Long fundingId;
    private Integer amount;
    private String facilityName;
}
