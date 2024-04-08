package com.ieum.common.format.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FailedCode {

    /* Global Server */
    RAISED_UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "처리 중 예기치 않은 서버 오류가 발생했습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

    /* JWT */
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "새로운 로그인이 필요합니다. 재로그인을 진행해주세요."),

    /* Authentication and Access */
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다. 로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTHENTICATION_2FA_FAILED(HttpStatus.UNAUTHORIZED, "2차 비밀번호 인증에 실패하였습니다."),

    /* Member */
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 가입되어 있는 전화번호입니다."),
    UNVERIFIED_PHONE_NUMBER(HttpStatus.UNAUTHORIZED, "인증되지 않은 전화번호입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 혹은 패스워드 정보가 정확하지 않습니다."),
    DUPLICATE_NICKNAME_CHANGE(HttpStatus.BAD_REQUEST, "이전 닉네임과 동일한 닉네임입니다."),

    /* Pay */
    PAYMONEY_CREATION_FAILED(HttpStatus.BAD_REQUEST, "페이머니 생성에 실패하였습니다."),
    PAYMENT_FAILED_INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액 부족으로 결제에 실패하였습니다"),
    PAYMENT_FAILED_EXCEEDED_LIMIT(HttpStatus.BAD_REQUEST, "카드 한도 초과로 결제에 실패하였습니다"),

    /* Card */
    PAYMENT_REGISTERED_CARD_NULL(HttpStatus.BAD_REQUEST, "연결된 카드가 없습니다."),
    REGISTERED_CARD_DELETE(HttpStatus.BAD_REQUEST, "메인 카드를 변경해주세요"),
    PAYMENT_FAILED_EXPIRED_CARD(HttpStatus.BAD_REQUEST, "카드 유효기간 만료로 결제에 실패하였습니다"),
    PAYMENT_FAILED_LOST_CARD(HttpStatus.BAD_REQUEST, "분실된 카드로 인해 결제에 실패하였습니다"),
    PAYMENT_FAILED_BANK_MAINTENANCE(HttpStatus.SERVICE_UNAVAILABLE, "은행 점검으로 인해 결제에 실패하였습니다"),

    /* Token and Cookie Operations */
    TOKEN_OPERATION_FAILED(HttpStatus.UNAUTHORIZED, "토큰 작업 중 예외가 발생했습니다."),
    COOKIE_OPERATION_FAILED(HttpStatus.BAD_REQUEST, "쿠키 작업 중 예외가 발생했습니다."),

    /* Security */
    INVALID_PRINCIPAL_TYPE(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 주체 유형입니다."),

    /* Donation */
    DONATION_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 후원 기록을 찾을 수 없습니다."),

    /* Funding */
    FUNDING_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 후원 결과를 찾을 수 없습니다."),

    /* Second Authentication */
    SECOND_AUTH_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "2차 인증 정보가 레디스에 저장되어 있지 않습니다."),
    SECOND_AUTH_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "2차 인증 검증에 실패하였습니다."),

    ;


    private final HttpStatus status;
    private final String message;

}


