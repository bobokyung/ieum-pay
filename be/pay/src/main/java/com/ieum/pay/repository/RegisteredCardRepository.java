package com.ieum.pay.repository;

import com.ieum.pay.domain.RegisteredCards;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredCardRepository extends JpaRepository<RegisteredCards, Long> {

    List<RegisteredCards> findByMemberId(Long memberId);

    Optional<RegisteredCards> findByMemberIdAndRegisteredCardId(Long memberId, Long registeredCardId);

    Optional<RegisteredCards> findByRegisteredCardId(Long id);
}
