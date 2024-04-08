package com.ieum.pay.service;

import com.ieum.pay.domain.Cards;
import com.ieum.pay.domain.RegisteredCards;
import com.ieum.pay.repository.RegisteredCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CardRegistrationService {

    private final RegisteredCardRepository registeredCardRepository;

    public Long registerCard(Cards card, Long memberId, String cardNickname) {
        RegisteredCards registeredCard = RegisteredCards.builder()
                                                        .cardId(card.getCardId())
                                                        .cardNickname(cardNickname)
                                                        .cardIssuer(card.getIssuer())
                                                        .memberId(memberId)
                                                        .build();
        return registeredCardRepository.save(registeredCard).getRegisteredCardId();
    }

    public List<RegisteredCards> getCardList(Long memberId) {
        return registeredCardRepository.findByMemberId(memberId);
    }

    public boolean isMyCard(Long memberId, Long registeredCardId) {
        return registeredCardRepository.findByMemberIdAndRegisteredCardId(memberId, registeredCardId).isPresent();
    }

    public boolean deleteCard(Long memberId, Long registeredCardId) {
        RegisteredCards registeredCard = registeredCardRepository.findByMemberIdAndRegisteredCardId(memberId, registeredCardId)
                                                                 .orElseThrow(
                                                                     () -> new IllegalArgumentException("Invalid member or registered card"));
        registeredCardRepository.delete(registeredCard);
        return true;
    }
}
