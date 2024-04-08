package com.ieum.pay.request;

public record CardRegisterRequest(String cardNumber, Long memberId, String cardNickname) {

}