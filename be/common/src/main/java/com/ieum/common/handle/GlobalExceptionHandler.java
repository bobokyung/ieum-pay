package com.ieum.common.handle;

import com.ieum.common.exception.auth.SecondAuthInfoNotFoundException;
import com.ieum.common.exception.cookie.CookieOperationException;
import com.ieum.common.exception.feign.PaymentServiceUnavailableException;
import com.ieum.common.exception.funding.FundingResultNotFoundException;
import com.ieum.common.exception.pay.DonationHistoryNotFoundException;
import com.ieum.common.exception.member.DuplicateNicknameChangeException;
import com.ieum.common.exception.member.ExistingPhoneNumberException;
import com.ieum.common.exception.member.InvalidPhoneNumberException;
import com.ieum.common.exception.member.InvalidPrincipalTypeException;
import com.ieum.common.exception.member.MemberNotFoundByIdException;
import com.ieum.common.exception.member.MemberNotFoundByPhoneNumberException;
import com.ieum.common.exception.member.MemberNotFoundException;
import com.ieum.common.exception.member.PasswordMismatchException;
import com.ieum.common.exception.pay.PayCardNotFoundException;
import com.ieum.common.exception.token.TokenOperationException;
import com.ieum.common.format.code.FailedCode;
import com.ieum.common.format.response.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ResponseTemplate response;

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handle(RuntimeException e) {
        e.printStackTrace();
        log.error("RuntimeException = {}", e.getMessage());
        return response.error(FailedCode.RAISED_UNEXPECTED_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return response.fail(e.getBindingResult(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<?> handle(MemberNotFoundException e) {
        log.error("MemberNotFoundException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(MemberNotFoundByIdException.class)
    protected ResponseEntity<?> handle(MemberNotFoundByIdException e) {
        log.error("MemberNotFoundByIdException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(MemberNotFoundByPhoneNumberException.class)
    protected ResponseEntity<?> handle(MemberNotFoundByPhoneNumberException e) {
        log.error("MemberNotFoundByPhoneNumberException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    protected ResponseEntity<?> handle(PasswordMismatchException e) {
        log.error("PasswordMismatchException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(ExistingPhoneNumberException.class)
    protected ResponseEntity<?> handle(ExistingPhoneNumberException e) {
        log.error("ExistingPhoneNumberException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    protected ResponseEntity<?> handle(InvalidPhoneNumberException e) {
        log.error("InvalidPhoneNumberException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(TokenOperationException.class)
    protected ResponseEntity<?> handle(TokenOperationException e) {
        log.error("TokenOperationException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(CookieOperationException.class)
    protected ResponseEntity<?> handle(CookieOperationException e) {
        log.error("CookieOperationException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(DuplicateNicknameChangeException.class)
    protected ResponseEntity<?> handle(DuplicateNicknameChangeException e) {
        log.error("DuplicateNicknameChangeException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(InvalidPrincipalTypeException.class)
    protected ResponseEntity<?> handle(InvalidPrincipalTypeException e) {
        log.error("InvalidPrincipalTypeException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(PaymentServiceUnavailableException.class)
    protected ResponseEntity<?> handle(PaymentServiceUnavailableException e) {
        log.error("PaymentServiceUnavailableException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(DonationHistoryNotFoundException.class)
    protected ResponseEntity<?> handle(DonationHistoryNotFoundException e) {
        log.error("DonationHistoryNotFoundException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(FundingResultNotFoundException.class)
    protected ResponseEntity<?> handle(FundingResultNotFoundException e) {
        log.error("FundingResultNotFoundException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(PayCardNotFoundException.class)
    protected ResponseEntity<?> handle(PayCardNotFoundException e) {
        log.error("PayCardNotFoundException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

    @ExceptionHandler(SecondAuthInfoNotFoundException.class)
    protected ResponseEntity<?> handle(SecondAuthInfoNotFoundException e) {
        log.error("SecondAuthInfoNotFoundException = {}", e.getFailedCode().getMessage());
        return response.error(e.getFailedCode());
    }

}
