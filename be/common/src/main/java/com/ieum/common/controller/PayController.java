package com.ieum.common.controller;

import com.ieum.common.annotation.CurrentMemberId;
import com.ieum.common.dto.request.PayRemittancePaymoneyRequestDTO;
import com.ieum.common.exception.auth.SecondAuthInfoNotFoundException;
import com.ieum.common.format.code.SuccessCode;
import com.ieum.common.format.response.ResponseTemplate;
import com.ieum.common.service.AuthService;
import com.ieum.common.service.MemberService;
import com.ieum.common.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "pay", description = "페이 API - 목업")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pay")
public class PayController {

    private final PayService payService;
    private final MemberService memberService;
    private final ResponseTemplate response;
    private final AuthService authService;


    @Operation(summary = "메인 페이지 조회", description = "사용자의 카드 정보와 페이머니, 기부 총액 조회")
    @ApiResponse(responseCode = "200", description = "메인 페이지 조회 성공")
    @GetMapping("/main")
    public ResponseEntity<?> getMainPage(
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(payService.getMainPageInfo(memberId), SuccessCode.MAIN_PAGE_FETCH_SUCCESSFUL);
    }

    @Operation(summary = "페이먼트 잔액 조회", description = "사용자의 페이머트 잔액을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "페이먼트 잔액 조회 성공")
    @GetMapping("balance")
    public ResponseEntity<?> getPaymoney(
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(payService.nowMyPaymoney(memberId), SuccessCode.PAYMONEY_BALANCE_FETCHED);
    }

    @Operation(summary = "사용 내역 조회", description = "지정된 기간 동안의 사용 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용 내역 조회 성공")
    @GetMapping("history/{period}")
    public ResponseEntity<?> getHistoryList(
        @Parameter(hidden = true) @CurrentMemberId Long memberId,
        @PathVariable("period") String period
    ) {
        return response.success(memberService.getHistoryList(memberId), SuccessCode.HISTORY_LIST_FETCHED);
    }

    @Operation(summary = "송금 내역 조회", description = "특정 송금 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "송금 내역 조회 성공")
    @GetMapping("history/remittance/{historyId}")
    public ResponseEntity<?> getHistory(
        @Parameter(hidden = true) @CurrentMemberId Long memberId,
        @PathVariable("historyId") Long id
    ) {
        return response.success(payService.getRemittanceHistory(memberId, id), SuccessCode.REMITTANCE_HISTORY_FETCHED);
    }

    @Operation(summary = "페이머니 송금", description = "페이머니를 송금합니다.")
    @ApiResponse(responseCode = "200", description = "페이머니 송금 성공")
    @PostMapping("remittance/paymoney")
    public ResponseEntity<?> sendPaymoney(
        @Parameter(hidden = true) @CurrentMemberId Long memberId,
        @RequestBody PayRemittancePaymoneyRequestDTO request
    ) {
        if (!authService.checkAuthInRedis(memberId, request.getAuthenticationKey())) {
            throw new SecondAuthInfoNotFoundException();
        }
        return response.success(payService.remmitancePaymoney(memberId, request), SuccessCode.PAYMONEY_REMITTANCE_SUCCESS);
    }

}
