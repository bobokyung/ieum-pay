package com.ieum.common.exception.pay;

import com.ieum.common.format.code.FailedCode;
import lombok.Getter;

@Getter
public class PayCardNotFoundException extends RuntimeException {

    private final FailedCode failedCode;

    public PayCardNotFoundException() {
        this.failedCode = FailedCode.PAYMENT_REGISTERED_CARD_NULL;
    }
}
