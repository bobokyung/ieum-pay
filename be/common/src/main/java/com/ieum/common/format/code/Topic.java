package com.ieum.common.format.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topic {

    TRANSFER_RECEIVED("transfer-received"),
    FUNDING_COMPLETED("funding-completed"),
    FCM_CONNECT("fcm-connect"),
    ;

    private final String topicName;
}
