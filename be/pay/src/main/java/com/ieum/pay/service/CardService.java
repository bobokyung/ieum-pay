package com.ieum.pay.service;

import com.ieum.pay.domain.Cards;
import com.ieum.pay.domain.RegisteredCards;
import com.ieum.pay.repository.CardRepository;
import com.ieum.pay.repository.RegisteredCardRepository;
import com.ieum.pay.response.CardDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final RegisteredCardRepository registeredCardRepository;

    @Transactional(readOnly = true)
    public Cards findCardByNumber(String cardNumber) {
        int cardNumFour = Integer.parseInt(cardNumber.substring(0, 6));
        int cardNumSix = Integer.parseInt(cardNumber.substring(0, 8));
        List<Cards> cardsList = cardRepository.findByCardNumbers(List.of(cardNumFour, cardNumSix));

        Cards cards = new Cards(4000L, "이음페이", "이음카드", 0, 0);
        for (Cards c : cardsList) {
            int bin = c.getCardBin();

            if (bin < 1000000) {
                return c;
            }

            if (cardNumSix == bin) {
                return c;
            }
        }

        return cards;
    }

    @Transactional(readOnly = true)
    public String getCardName(Long id) {
        RegisteredCards card = registeredCardRepository.findByRegisteredCardId(id).orElse(null);
        return card != null ? card.getCardNickname() : null;
    }

    @Transactional(readOnly = true)
    public CardDetailResponse getCardCompany(String number) {
        List<Cards> cardsList = cardRepository.findByCardNumbers(
            List.of(Integer.parseInt(number.substring(0, 6)), Integer.parseInt(number.substring(0, 8)))
        );

        if (cardsList.isEmpty()) {
            return CardDetailResponse.builder().build();
        }

        Cards cards = cardsList.get(0);
        return CardDetailResponse.builder()
                                 .issuer(cards.getIssuer())
                                 .product(cards.getCardProduct())
                                 .build();
    }

    public boolean delete(Long memberId, Long id) {
        // 성공 여부를 저장할 변수 선언
        // 찾지 못한 경우
        return registeredCardRepository.findByMemberIdAndRegisteredCardId(memberId, id)
                                       .map(registeredCards -> {
                                           registeredCardRepository.delete(registeredCards);
                                           return true; // 삭제 성공
                                       })
                                       .orElse(false);
    }
}