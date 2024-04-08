package com.ieum.pay.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cards {

    @Id
    private Long cardId;

    private String issuer;

    private String cardProduct;

    private int isCorporate;

    private int cardBin;

    private static final int NICKNAME_SUFFIX_START = 12;

    public String generateDefaultNickname(String cardNumber) {
        return getCardProduct() + cardNumber.substring(NICKNAME_SUFFIX_START);
    }
}
