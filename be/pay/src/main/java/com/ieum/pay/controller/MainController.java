package com.ieum.pay.controller;

import com.ieum.pay.domain.Paymoney;
import com.ieum.pay.domain.RegisteredCards;
import com.ieum.pay.dto.CardDTO;
import com.ieum.pay.response.MainSummaryResponseDTO;
import com.ieum.pay.service.PaymoneyService;
import com.ieum.pay.service.CardRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/main/summary")
@RequiredArgsConstructor
public class MainController {
    private final PaymoneyService paymoneyService;
    private final CardRegistrationService cardRegistrationService;
    @GetMapping("{memberId}")
    public MainSummaryResponseDTO getMainSummary(@PathVariable Long memberId){
        Paymoney paymoney = paymoneyService.getPaymoney(memberId);
        List<RegisteredCards> cardsList = cardRegistrationService.getCardList(memberId);
        MainSummaryResponseDTO result = new MainSummaryResponseDTO(paymoney.getPaymoneyAmount(), paymoney.getDonationTotalAmount());

        for(RegisteredCards c : cardsList){
            result.getCardList().add(CardDTO.builder()
                    .registeredCardId(c.getRegisteredCardId())
                    .cardId(c.getCardId())
                    .cardNickname(c.getCardNickname())
                    .cardIssuer(c.getCardIssuer())
                    .mainCard(false)
                    .build());
        }

        return result;
    }
}
