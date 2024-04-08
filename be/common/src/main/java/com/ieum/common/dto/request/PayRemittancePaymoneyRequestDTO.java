package com.ieum.common.dto.request;

import lombok.Data;

@Data
public class PayRemittancePaymoneyRequestDTO {

    private String authenticationKey;
    private String phoneNumber;
    private int amount;
}
