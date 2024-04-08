package com.ieum.common.dto.response;

import com.ieum.common.dto.FundingMemberDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingCompleteDetailResponseDTO {

    String facilityName;
    String facilityAddress;
    String facilityPhoneNumber;
    String facilityRepresentativeName;
    String facilityRepresentativePhoneNumber;
    int facilityCapacity;
    String facilityImage;
    String fundingOpenDate;
    int fundingPeopleCnt;
    String fundingTitle;
    int goalAmount;
    List<FundingMemberDTO> fundingMemberList;

}
