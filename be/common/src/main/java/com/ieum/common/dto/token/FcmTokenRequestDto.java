package com.ieum.common.dto.token;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenRequestDto {

    private String fcmToken;
}

