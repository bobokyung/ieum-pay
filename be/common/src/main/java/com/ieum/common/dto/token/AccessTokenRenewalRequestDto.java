package com.ieum.common.dto.token;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessTokenRenewalRequestDto {

    @NotBlank(message = "이전 토큰 값이 반드시 필요합니다.")
    private String accessToken;
}
