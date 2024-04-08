package com.ieum.pay.controller;

import com.ieum.pay.domain.Cards;
import com.ieum.pay.request.CardRegisterRequest;
import com.ieum.pay.request.CardValidRequest;
import com.ieum.pay.response.CardDetailResponse;
import com.ieum.pay.service.CardRegistrationService;
import com.ieum.pay.service.CardService;
import com.ieum.pay.service.CardValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardRegistrationService cardRegistrationService;
    private final CardValidationService cardValidationService;

    @PostMapping("/valid")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerCard(@RequestBody CardRegisterRequest request) {
        String cardNumber = request.cardNumber();
        if (!cardValidationService.isValidCardNumber(cardNumber)) {
            return -1L;
//            throw new IllegalArgumentException("Invalid card number");
        }

        Cards card = cardService.findCardByNumber(cardNumber);
        String nickname = request.cardNickname() != null && !request.cardNickname().equals("") ? request.cardNickname() : card.generateDefaultNickname(cardNumber);
        return cardRegistrationService.registerCard(card, request.memberId(), nickname);
    }

    @DeleteMapping("/{registeredCardId}/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteCard(@PathVariable("registeredCardId") Long registeredCardId,
                           @PathVariable("memberId") Long memberId) {
        return cardRegistrationService.deleteCard(memberId, registeredCardId);
    }

    @GetMapping("/{registeredCardId}/name")
    public String getCardName(@PathVariable Long registeredCardId) {
        return cardService.getCardName(registeredCardId);
    }

    @GetMapping("/company")
    public CardDetailResponse getCardCompany(@RequestParam String cardNumber) {
        return cardService.getCardCompany(cardNumber);
    }
}