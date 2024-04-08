package com.ieum.funding.controller;

import com.ieum.funding.repository.FundingMembersRepository;
import com.ieum.funding.request.AutoDonationRequestDTO;
import com.ieum.funding.request.DirectlyDonationRequestDTO;
import com.ieum.funding.request.FundingLinkRequestDTO;
import com.ieum.funding.request.FundingLinkupRequestDTO;
import com.ieum.funding.response.AutoFundingResultResponseDTO;
import com.ieum.funding.response.CurrentFundingResult1DTO;
import com.ieum.funding.response.CurrentFundingResult2DTO;
import com.ieum.funding.response.CurrentFundingResultResponseDTO;
import com.ieum.funding.response.FundingInfoResponseDTO;
import com.ieum.funding.response.FundingDetailResponseDTO;
import com.ieum.funding.response.FundingDonationResponseDTO;
import com.ieum.funding.response.FundingReceiptResponseDTO;
import com.ieum.funding.response.FundingSummaryResponseDTO;
import com.ieum.funding.response.FundingResultResponseDTO;
import com.ieum.funding.service.FundingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    @Operation(summary = "펀딩 상세 조회", description = "선택한 펀딩의 상세 정보를 조회")
    @GetMapping("/{fundingId}/{memberId}/detail")
    public ResponseEntity<FundingDetailResponseDTO> getFundingOngoingDetail(
        @PathVariable("fundingId") Long fundingId,
        @PathVariable("memberId") Long memberId) {
        FundingDetailResponseDTO response = fundingService.getFundingDetail(fundingId, memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "진행 중인 펀딩 목록 조회", description = "현재 진행 중인 모든 펀딩의 목록을 조회합니다.")
    @GetMapping("/list/ongoing")
    public ResponseEntity<List<FundingSummaryResponseDTO>> getFundingOngoingList() {
        List<FundingSummaryResponseDTO> response = fundingService.getFundingOngoingList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "완료 펀딩 목록 조회", description = "완료된 모든 펀딩의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "완료된 목록 조회 성공")
    @GetMapping("/list/complete")
    public ResponseEntity<List<FundingSummaryResponseDTO>> getFundingCompleteList() {
        List<FundingSummaryResponseDTO> response = fundingService.getFundingCompleteList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "펀딩 연계", description = "사용자를 특정 펀딩에 연계시킵니다.")
    @ApiResponse(responseCode = "200", description = "펀딩 연계 성공")
    @PostMapping("/linkup")
    public Boolean fundingLinkup(@RequestBody FundingLinkupRequestDTO request) {
        fundingService.linkupFunding(request);
        return true;
    }

    @Operation(summary = "펀딩 연계 해제", description = "특정 펀딩에 대한 연계를 해제합니다")
    @ApiResponse(responseCode = "200", description = "펀딩 연계 해제 성공")
    @PostMapping("/unlink")
    public Boolean fundingUnlink(@RequestBody FundingLinkRequestDTO request) {
        fundingService.unlinkFunding(request.getFundingId(), request.getMemberId());
        return true;
    }

    @Operation(summary = "직접기부 결제 정보 요청", description = "직접기부 결제시 해당 결제에 대한 정보 요청")
    @ApiResponse(responseCode = "200", description = "정보 조회 성공")
    @GetMapping("/info/directly/{fundingId}")
    public ResponseEntity<FundingInfoResponseDTO> getDirectlyFundingInfo(
        @PathVariable("fundingId") Long fundingId) {
        FundingInfoResponseDTO response = fundingService.getDonationInfo(fundingId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직접 기부", description = "펀딩에 직접 기부합니다.")
    @ApiResponse(responseCode = "200", description = "펀딩 기부 성공 - Boolean - fundingResult 반환")
    @PostMapping("/donation/directly")
    public ResponseEntity<FundingDonationResponseDTO> donationDirectly(
        @RequestBody DirectlyDonationRequestDTO request) {
        FundingDonationResponseDTO response = FundingDonationResponseDTO.builder()
                                                                        .fundingId(request.getFundingId())
                                                                        .fundingResult(fundingService.directDonation(request.getFundingId(),
                                                                                                                     request.getMemberId(),
                                                                                                                     request.getAmount(),
                                                                            request.getNickname()))
                                                                        .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "자동기부 결제 정보 요청", description = "자동기부 결제시 해당 결제에 대한 정보 요청")
    @ApiResponse(responseCode = "200", description = "정보 조회 성공")
    @GetMapping("/info/auto/{memberId}")
    public ResponseEntity<FundingInfoResponseDTO> getAutoFundingInfo(@PathVariable("memberId") Long memberId) {
        FundingInfoResponseDTO response = fundingService.getAutoDonationInfo(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "펀딩 결과 정보 조회", description = "결제 완료 내역 조회, 직접 기부 완료 조회, 모금 연동 완료")
    @ApiResponse(responseCode = "200", description = "facilityName 반환")
    @GetMapping("/result/{fundingId}")
    public ResponseEntity<FundingResultResponseDTO> getPaymentResult(
        @PathVariable("fundingId") Long fundingId) {
        FundingResultResponseDTO response = fundingService.getFacilityInfo(fundingId);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "자동 기부", description = "펀딩에 자동 기부합니다.")
    @ApiResponse(responseCode = "200", description = "자동기부 성공 - Integer - amount 반환(기부 금액)")
    @PostMapping("/donation/auto")
    public ResponseEntity<AutoFundingResultResponseDTO> donationAuto(
        @RequestBody AutoDonationRequestDTO request) {
        AutoFundingResultResponseDTO response = fundingService.autoDonation(request.getMemberId(), request.getAmount());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "기부 영수증", description = "기부 영수증")
    @ApiResponse(responseCode = "200", description = "영수증 정보 출력 fac.title, fac.Name, fac.FP, Sp.name")
    @GetMapping("/receipt/{fundingId}")
    public ResponseEntity<FundingReceiptResponseDTO> getReceiptInfo(
        @PathVariable("fundingId") Long fundingId) {
        FundingReceiptResponseDTO response = fundingService.getReceiptInfo(fundingId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "현재 기부 내역", description = "현재 기부 내역")
    @ApiResponse(responseCode = "200", description = "현재 기부 내역")
    @GetMapping("/info/current/{memberId}")
    public ResponseEntity<CurrentFundingResultResponseDTO> getCurrentFundingInfo(
        @PathVariable("memberId") Long memberId) {
        CurrentFundingResult1DTO res1 = fundingService.getCurrentFunding1(memberId);
        CurrentFundingResult2DTO res2 = fundingService.getCurrentFunding2(memberId);

        if (res1 == null) {
            return ResponseEntity.ok(CurrentFundingResultResponseDTO.builder()
                                                                    .fundingId(0L)
                                                                    .facilityName("")
                                                                    .facilityImage("")
                                                                    .fundingTotalAmount(0)
                                                                    .fundingCount(res2.getFundingCount())
                                                                    .build());
        }

        return ResponseEntity.ok(CurrentFundingResultResponseDTO.builder()
                                                                .fundingId(res1.getFundingId())
                                                                .facilityName(res1.getFacilityName())
                                                                .facilityImage(res1.getFacilityImage())
                                                                .fundingTotalAmount(res1.getFundingTotalAmount())
                                                                .fundingCount(res2.getFundingCount())
                                                                .build());
    }

    @Operation(summary = "참여한 목록 조회", description = "참여한 펀딩의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "참여한 목록 조회 성공")
    @GetMapping("/list/{memberId}")
    public ResponseEntity<List<FundingSummaryResponseDTO>> getFundingParticipantList(@PathVariable("memberId") Long memberId) {
        List<FundingSummaryResponseDTO> response = fundingService.getFundingParticipantList(memberId);
        return ResponseEntity.ok(response);
    }
}
