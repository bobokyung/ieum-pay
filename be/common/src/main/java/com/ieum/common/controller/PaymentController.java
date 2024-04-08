package com.ieum.common.controller;

import static com.ieum.common.format.code.FailedCode.INVALID_PRINCIPAL_TYPE;
import static com.ieum.common.format.code.FailedCode.PAYMENT_REGISTERED_CARD_NULL;

import com.ieum.common.annotation.CurrentMemberId;
import com.ieum.common.domain.Members;
import com.ieum.common.dto.request.MemberPaypwUpdateRequestDTO;
import com.ieum.common.dto.request.PaymentRequestDTO;
import com.ieum.common.format.code.SuccessCode;
import com.ieum.common.format.response.ResponseTemplate;
import com.ieum.common.service.AuthService;
import com.ieum.common.service.MemberService;
import com.ieum.common.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "payment", description = "결제 API - 목업")
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;
    private final ResponseTemplate response;
    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "결제 처리", description = "결제를 처리합니다.")
    @ApiResponse(responseCode = "200", description = "결제 처리 성공")
    @PostMapping
    public ResponseEntity<?> payment(
        @RequestBody PaymentRequestDTO requestDTO,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {

        if (!authService.checkAuthInRedis(memberId, requestDTO.getAuthenticationKey())) {
            return response.error(INVALID_PRINCIPAL_TYPE);
        }

        Members member = memberService.findMemberById(memberId);
        if (member.getPaycardId() == null) {
            return response.error(PAYMENT_REGISTERED_CARD_NULL);
        }
        return response.success(paymentService.processPayment(memberId, requestDTO), SuccessCode.PAYMENT_PROCESS_SUCCESSFUL);
    }


    @Operation(summary = "결제 비밀번호 변경", description = "회원의 결제 비밀번호를 변경합니다. - 수정 필요")
    @ApiResponse(responseCode = "200", description = "결제 비밀번호 변경 성공")
    @PutMapping("/pay-pw/update")
    public ResponseEntity<?> updatePaymentPassword(
        @RequestBody MemberPaypwUpdateRequestDTO request,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        boolean authCheck = authService.checkAuthInRedis(memberId, request.getAuthenticationKey());
        if (!authCheck) {
            return response.error(INVALID_PRINCIPAL_TYPE);
        }
        return response.success(
            paymentService.updatePaymentPassword(memberId, passwordEncoder.encode(request.getNewPaymentPassword())),
            SuccessCode.PAYMENT_PASSWORD_UPDATE_SUCCESS);
    }


    @Operation(summary = "결제 내역 조회", description = "특정 결제 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 내역 조회 성공")
    @GetMapping("{historyId}")
    public ResponseEntity<?> getPaymentHistory(
        @PathVariable("historyId") Long id,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(paymentService.getPaymentHistory(memberId, id), SuccessCode.PAYMENT_HISTORY_FETCHED);
    }


    @Operation(summary = "결제 정보 조회", description = "특정 매장과 가격에 따른 결제 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 정보 조회 성공")
    @GetMapping("info/{store}/{price}")
    public ResponseEntity<?> getPaymentInfo(
        @PathVariable("store") Long storeId,
        @PathVariable("price") int price,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(paymentService.getPaymentInfo(memberId, storeId, price), SuccessCode.PAYMENT_INFO_FETCHED);
    }
}
