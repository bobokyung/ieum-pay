package com.ieum.common.exception.pay;

import com.ieum.common.format.code.FailedCode;
import lombok.Getter;

@Getter
public class PayMoneyCreationFailedException extends RuntimeException {

    private final FailedCode failedCode;

    public PayMoneyCreationFailedException() {
        this.failedCode = FailedCode.PAYMONEY_CREATION_FAILED;
    }
}
