package com.ieum.common.feign;

import com.ieum.common.dto.feign.funding.request.AutoDonationRequestDTO;
import com.ieum.common.dto.feign.funding.request.FundingDonationRequestDTO;
import com.ieum.common.dto.feign.funding.request.FundingLinkupRequestDTO;
import com.ieum.common.dto.feign.funding.request.FundingUnlinkRequestDTO;
import com.ieum.common.dto.feign.funding.response.AutoFundingResultResponseDTO;
import com.ieum.common.dto.feign.funding.response.CurrentFundingResultResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingDetailResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingDonationResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingInfoResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingReceiptResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingResultResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingSummaryResponseDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "donation", url = "${gateway.donation}")
public interface FundingFeignClient {

    // 펀딩 상세 조회
    @GetMapping(value = "/funding/{fundingId}/{memberId}/detail")
    FundingDetailResponseDTO getFundingDetail(@PathVariable("fundingId") Long fundingId,
                                              @PathVariable("memberId") Long memberId);

    // 펀딩 진행중 목록 조회
    @GetMapping(value = "/funding/list/ongoing")
    List<FundingSummaryResponseDTO> getFundingOngoingList();

    // 펀딩 완료 목록 조회
    @GetMapping(value = "/funding/list/complete")
    List<FundingSummaryResponseDTO> getFundingCompleteList();

    // 펀딩 연동
    @PostMapping(value = "/funding/linkup")
    Boolean linkup(@RequestBody FundingLinkupRequestDTO request);

    // 펀딩 연동 해제
    @PostMapping(value = "/funding/unlink")
    Boolean unlink(@RequestBody FundingUnlinkRequestDTO request);

    // 직접 기부 결제 정보 요청
    @GetMapping(value = "/funding/info/directly/{fundingId}")
    FundingInfoResponseDTO getDirectlyFundingInfo(@PathVariable("fundingId") Long fundingId);

    // 자동 기부 결제 정보 요청
    @GetMapping(value = "/funding/info/auto/{memberId}")
    FundingInfoResponseDTO getAutoFundingInfo(@PathVariable("memberId") Long memberId);

    // 직접 기부
    @PostMapping(value = "/funding/donation/directly")
    FundingDonationResponseDTO donationDirectly(@RequestBody FundingDonationRequestDTO request);

    // 자동 기부
    @PostMapping(value = "/funding/donation/auto")
    AutoFundingResultResponseDTO donationAuto(@RequestBody AutoDonationRequestDTO request);

    // 펀딩 결과 정보 조회
    @GetMapping(value = "/funding/result/{fundingId}")
    Optional<FundingResultResponseDTO> getPaymentResult(@PathVariable("fundingId") Long fundingId);

    // 영수증
    @GetMapping(value = "/funding/receipt/{fundingId}")
    FundingReceiptResponseDTO getReceipt(@PathVariable("fundingId") Long fundingId);

    @GetMapping("/funding/info/current/{memberId}")
    CurrentFundingResultResponseDTO getCurrentInfo(@PathVariable("memberId") Long memberId);

    @GetMapping(value = "/funding/list/{memberId}")
    List<FundingSummaryResponseDTO> getFundingParticipationList(@PathVariable("memberId") Long memberid);
}
