package com.ieum.funding.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectlyDonationRequestDTO {
    private Long fundingId;
    private Long memberId;
    private Integer amount;
    private String nickname;
}
