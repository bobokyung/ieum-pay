package com.ieum.common.controller;

import com.ieum.common.annotation.CurrentMemberId;
import com.ieum.common.format.code.SuccessCode;
import com.ieum.common.format.response.ResponseTemplate;
import com.ieum.common.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "main", description = "main API - 목업")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final MemberService memberService;
    private final ResponseTemplate response;

    @Operation(summary = "메인 페이지 조회", description = "메인페이지 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메인페이지")
    @GetMapping("/summary")
    public ResponseEntity<?> getMainSummary(
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(memberService.getMainSummary(memberId), SuccessCode.MAIN_PAGE_SUMMARY_FETCHED);
    }

}
