package com.ieum.funding.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundingMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingMemberId;

    private Long memberId;

    private Long fundingId;

    private int fundingTotalAmount;

    private String nickname;

    @Builder.Default
    private boolean isAutoFundingStatus = true;

}
