package com.ieum.common.exception.auth;

import com.ieum.common.format.code.FailedCode;
import lombok.Getter;

@Getter
public class SecondAuthInfoNotFoundException extends RuntimeException {

    private final FailedCode failedCode;

    public SecondAuthInfoNotFoundException() {
        this.failedCode = FailedCode.SECOND_AUTH_INFO_NOT_FOUND;
    }
}
