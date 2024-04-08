package com.ieum.funding.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentFundingResult1DTO {

    private Long fundingId;
    private String facilityName;
    private String facilityImage;
    private int fundingTotalAmount;

}
