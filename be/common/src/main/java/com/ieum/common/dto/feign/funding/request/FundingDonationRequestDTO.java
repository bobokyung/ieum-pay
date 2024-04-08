package com.ieum.common.dto.feign.funding.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingDonationRequestDTO {
    private Long fundingId;
    private Long memberId;
    private Integer amount;
    private String nickname;
}
