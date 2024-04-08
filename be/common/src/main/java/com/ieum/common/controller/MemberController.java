package com.ieum.common.controller;

import com.ieum.common.annotation.CurrentMemberId;
import com.ieum.common.dto.member.req.LoginRequestDto;
import com.ieum.common.dto.member.req.NicknameUpdateRequestDto;
import com.ieum.common.dto.member.req.PasswordUpdateRequestDto;
import com.ieum.common.dto.member.req.SignupRequestDto;
import com.ieum.common.format.code.SuccessCode;
import com.ieum.common.format.response.ResponseTemplate;
import com.ieum.common.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "members", description = "멤버 API - 목업")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final ResponseTemplate response;

    @Operation(summary = "회원 정보 조회", description = "회원의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공, 회원 정보 반환")
    @GetMapping
    public ResponseEntity<?> getProfileById(
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(memberService.getProfileById(memberId), SuccessCode.MEMBER_PROFILE_FETCHED);
    }

    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원 등록 성공, 이름, 닉네임 반환")
    @PostMapping
    public ResponseEntity<?> signup(
        @RequestBody @Valid SignupRequestDto request
    ) {
        return response.success(memberService.signup(request), SuccessCode.MEMBER_SIGNUP_SUCCESSFUL);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 합니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(
        @Parameter(hidden = true) HttpServletRequest req,
        @Parameter(hidden = true) HttpServletResponse res,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(memberService.delete(memberId, req, res), SuccessCode.MEMBER_WITHDRAWAL_SUCCESSFUL);
    }

    @Operation(summary = "회원 존재 여부 확인", description = "회원의 존재 여부를 확인합니다.")
    @ApiResponse(responseCode = "200", description = "회원 존재 여부 exist:true/false")
    @GetMapping("/exist")
    public ResponseEntity<?> checkMember(
        @RequestParam("phone-number") String phoneNumber
    ) {
        return response.success(memberService.existsMemberByPhoneNumber(phoneNumber), SuccessCode.MEMBER_EXISTS_BY_PHONE_NUMBER);
    }

    @Operation(summary = "회원 로그인", description = "회원 로그인을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "멤버ID, 이름, 닉네임 반환")
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody @Valid LoginRequestDto request,
        @Parameter(hidden = true) HttpServletResponse servletResponse
    ) {
        return response.success(memberService.login(request, servletResponse), SuccessCode.MEMBER_LOGIN_SUCCESSFUL);
    }

    @Operation(summary = "회원 로그아웃", description = "회원 로그아웃을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "멤버ID, 이름, 닉네임 반환")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        @Parameter(hidden = true) HttpServletRequest req,
        @Parameter(hidden = true) HttpServletResponse res
    ) {
        return response.success(memberService.logout(req, res), SuccessCode.MEMBER_LOGOUT_SUCCESSFUL);
    }

    @Operation(summary = "회원 검색", description = "회원을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "회원 검색 성공 - 회원 이름, 전화번호")
    @PostMapping("/search")
    public ResponseEntity<?> search(
        @RequestParam("phone-number")
        @NotBlank(message = "휴대폰 번호는 필수입니다.")
        @Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호 형식이 잘못되었습니다.")
        String phoneNumber
    ) {
        return response.success(memberService.getRecipientByPhoneNumber(phoneNumber), SuccessCode.RECIPIENT_FETCH_SUCCESSFUL);
    }

    @Operation(summary = "비밀번호 변경", description = "회원의 로그인 비밀번호를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 비밀번호 변경 성공")
    @PutMapping("/password")
    public ResponseEntity<?> updateLoginPassword(
        @RequestBody @Valid PasswordUpdateRequestDto request,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        memberService.updatePassword(request, memberId);
        return response.success(SuccessCode.PASSWORD_CHANGE_SUCCESSFUL);
    }

    @Operation(summary = "닉네임 변경", description = "회원의 닉네임을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "닉네임 변경 성공")
    @PutMapping("/nickname")
    public ResponseEntity<?> updateNickname(
        @RequestBody NicknameUpdateRequestDto request,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(memberService.updateNickname(request, memberId), SuccessCode.NICKNAME_CHANGE_SUCCESSFUL);
    }

    @Operation(summary = "회원 요약 정보 조회", description = "회원의 요약 정보를 조회합니다. - 수정 필요")
    @ApiResponse(responseCode = "200", description = "회원 정보 요약(기부내역 포함)")
    @GetMapping("/summary")
    public ResponseEntity<?> getMemberSummary(
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        return response.success(memberService.getSummaryInfo(memberId), SuccessCode.MEMBER_SUMMARY_FETCHED);
    }
}
