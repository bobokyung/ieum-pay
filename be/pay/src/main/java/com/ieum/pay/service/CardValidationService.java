package com.ieum.pay.service;

import org.springframework.stereotype.Service;

@Service
public class CardValidationService {

    public boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        char[] cardNumberArray = cardNumber.toCharArray();
        int lastNumber = cardNumberArray[15] - '0';

        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int num = cardNumberArray[i] - '0';
            if (i % 2 == 0) {
                num = num * 2;
                if (num > 9) {
                    num = num - 9;
                }
            }
            sum += num;
        }

        sum += lastNumber;
        return sum % 10 == 0;
    }
}