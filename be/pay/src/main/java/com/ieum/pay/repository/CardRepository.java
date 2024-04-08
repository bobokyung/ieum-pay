package com.ieum.pay.repository;

import com.ieum.pay.domain.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Cards, Long> {

    @Query("SELECT c FROM Cards c " +
        "WHERE c.cardBin IN :numbers " +
        "ORDER BY c.cardBin ASC")
    List<Cards> findByCardNumbers(@Param("numbers") List<Integer> numbers);
}
