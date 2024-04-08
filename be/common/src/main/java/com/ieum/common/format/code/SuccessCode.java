package com.ieum.common.format.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    /* 공통 */
    SUCCESS(HttpStatus.OK, "작업이 성공적으로 완료되었습니다."),
    MAIN_PAGE_FETCH_SUCCESSFUL(HttpStatus.OK, "메인 페이지 조회가 성공적으로 완료되었습니다."),

    /* 회원 */
    MEMBER_SIGNUP_SUCCESSFUL(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),
    MEMBER_PROFILE_FETCHED(HttpStatus.OK, "회원 프로필 조회가 성공적으로 완료되었습니다."),
    MEMBER_WITHDRAWAL_SUCCESSFUL(HttpStatus.OK, "회원 탈퇴가 성공적으로 완료되었습니다."),
    MEMBER_EXISTS_BY_PHONE_NUMBER(HttpStatus.OK, "전화번호로 회원 존재 여부 확인이 성공적으로 완료되었습니다."),
    MEMBER_LOGOUT_SUCCESSFUL(HttpStatus.OK, "로그아웃이 성공적으로 완료되었습니다."),
    MEMBER_LOGIN_SUCCESSFUL(HttpStatus.OK, "로그인이 성공적으로 완료되었습니다."),
    NICKNAME_CHANGE_SUCCESSFUL(HttpStatus.OK, "닉네임 변경이 성공적으로 완료되었습니다."),
    PASSWORD_CHANGE_SUCCESSFUL(HttpStatus.OK, "비밀번호 변경이 성공적으로 완료되었습니다."),
    RECIPIENT_FETCH_SUCCESSFUL(HttpStatus.OK, "전화번호로 회원 조회가 성공적으로 완료되었습니다."),

    /* 결제 */
    PAYMENT_INITIATED(HttpStatus.CREATED, "결제가 성공적으로 시작되었습니다."),
    CARD_VALID_WRONG(HttpStatus.OK, "카드 번호가 잘못된 형식입니다."),
    CARD_TYPE_WRONG(HttpStatus.OK, "카드 번호가 16자리가 아니거나 숫자가 아닙니다."),
    CARD_REGISTER_SUCCESSFUL(HttpStatus.OK, "카드 등록이 완료되었습니다."),

    /* 인증 */
    AUTHENTICATION_CODE_ISSUED(HttpStatus.OK, "본인 확인용 인증 코드가 성공적으로 발급되었습니다."),
    ACCESS_TOKEN_RENEWED(HttpStatus.OK, "액세스 토큰 갱신이 성공적으로 완료되었습니다."),

    /* 기부 */
    DIRECT_DONATION_RESULT_SUCCESS(HttpStatus.OK, "직접 기부 결과 조회가 성공적으로 완료되었습니다."),

    HISTORY_LIST_FETCHED(HttpStatus.OK, "사용 내역 조회가 성공적으로 완료되었습니다."),
    REMITTANCE_HISTORY_FETCHED(HttpStatus.OK, "송금 내역 조회가 성공적으로 완료되었습니다."),
    PAYMONEY_REMITTANCE_SUCCESS(HttpStatus.OK, "페이머니 송금이 성공적으로 완료되었습니다."),

    /* 페이먼트 */
    PAYMONEY_BALANCE_FETCHED(HttpStatus.OK, "페이먼트 잔액 조회가 성공적으로 완료되었습니다."),
    MAIN_CARD_SETTING_SUCCESSFUL(HttpStatus.OK, "메인 카드 설정이 성공적으로 완료되었습니다."),
    CARD_OCR_SUCCESS(HttpStatus.OK, "카드 OCR 처리가 성공적으로 완료되었습니다."),
    CARD_UPDATE_SUCCESSFUL(HttpStatus.OK, "카드 정보 업데이트가 성공적으로 완료되었습니다."),

    /* 펀딩 */
    FUNDING_DETAIL_FETCHED(HttpStatus.OK, "펀딩 상세 정보 조회가 성공적으로 완료되었습니다."),
    FUNDING_ONGOING_LIST_FETCHED(HttpStatus.OK, "진행 중인 펀딩 목록 조회가 성공적으로 완료되었습니다."),
    FUNDING_COMPLETE_LIST_FETCHED(HttpStatus.OK, "완료된 펀딩 목록 조회가 성공적으로 완료되었습니다."),
    FUNDING_PARTICIPATION_LIST_FETCHED(HttpStatus.OK, "참여했던 펀딩 목록 조회가 성공적으로 완료되었습니다."),
    FUNDING_LINKUP_SUCCESS(HttpStatus.OK, "펀딩 연계가 성공적으로 완료되었습니다."),
    FUNDING_UNLINK_SUCCESS(HttpStatus.OK, "펀딩 연계 해제가 성공적으로 완료되었습니다."),
    FUNDING_LINK_RESULT_FETCHED(HttpStatus.OK, "펀딩 연동 결과 조회가 성공적으로 완료되었습니다."),
    DIRECT_DONATION_SUCCESS(HttpStatus.OK, "직접 기부가 성공적으로 완료되었습니다."),
    DIRECTLY_FUNDING_INFO_FETCHED(HttpStatus.OK, "직접기부 결제 정보 조회가 성공적으로 완료되었습니다."),
    RECEIPT_INFO_FETCHED(HttpStatus.OK, "영수증 정보 조회가 성공적으로 완료되었습니다."),
    CURRENT_INFO_FETCHED(HttpStatus.OK, "현재 정보 (기부 관련) 조회가 성공적으로 완료되었습니다."),

    /* 메인 페이지 */
    MAIN_PAGE_SUMMARY_FETCHED(HttpStatus.OK, "메인 페이지 정보 조회가 성공적으로 완료되었습니다."),

    /* 카드 관련 */
    CARD_OCR_PROCESS_SUCCESSFUL(HttpStatus.OK, "카드 OCR 처리가 성공적으로 완료되었습니다."),

    /* 회원 정보 */
    MEMBER_SUMMARY_FETCHED(HttpStatus.OK, "회원 요약 정보 조회가 성공적으로 완료되었습니다."),

    /* 결제 처리 */
    PAYMENT_PROCESS_SUCCESSFUL(HttpStatus.OK, "결제 처리가 성공적으로 완료되었습니다."),
    PAYMENT_PASSWORD_VERIFICATION_SUCCESS(HttpStatus.OK, "결제 비밀번호 인증이 성공적으로 완료되었습니다."),
    PAYMENT_PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "결제 비밀번호 변경이 성공적으로 완료되었습니다."),
    PAYMENT_HISTORY_FETCHED(HttpStatus.OK, "결제 내역 조회가 성공적으로 완료되었습니다."),
    PAYMENT_INFO_FETCHED(HttpStatus.OK, "결제 정보 조회가 성공적으로 완료되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;

}

