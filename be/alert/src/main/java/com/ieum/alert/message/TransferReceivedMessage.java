package com.ieum.alert.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferReceivedMessage {

    private Long senderId;
    private String senderName;
    private Long receiverId;
    private int amount;
}
