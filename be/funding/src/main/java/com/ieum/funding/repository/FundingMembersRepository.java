package com.ieum.funding.repository;

import com.ieum.funding.domain.FundingMembers;
import com.ieum.funding.dto.FundingMemberDTO;
import com.ieum.funding.response.CurrentFundingResult1DTO;
import com.ieum.funding.response.CurrentFundingResult2DTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FundingMembersRepository extends JpaRepository<FundingMembers, Long> {

    @Query("""
        SELECT new com.ieum.funding.dto.FundingMemberDTO(
        fm.nickname, fm.memberId, fm.fundingTotalAmount)\s
        FROM FundingMembers fm
        WHERE fm.fundingId = :fundingId AND fm.fundingTotalAmount > 0
        """)
    List<FundingMemberDTO> findByFundingId(Long fundingId);

    Optional<FundingMembers> findFirstByFundingIdAndMemberId(Long fundingId, Long memberId);

    @Modifying
    @Query("UPDATE FundingMembers f SET f.isAutoFundingStatus = false WHERE f.memberId = :memberId AND f.fundingId = :fundingId")
    void unlink(Long fundingId, Long memberId);

    @Modifying
    @Query("UPDATE FundingMembers f SET f.isAutoFundingStatus = false WHERE f.memberId = :memberId")
    void unlinkAll(Long memberId);

    @Modifying
    @Query("UPDATE FundingMembers f SET f.isAutoFundingStatus = true WHERE f.memberId = :memberId AND f.fundingId = :fundingId")
    void linkup(Long fundingId, Long memberId);

    @Modifying
    @Transactional
    @Query("UPDATE FundingMembers fm SET fm.fundingTotalAmount = fm.fundingTotalAmount + :amount WHERE fm.fundingId = :fundingId AND fm.memberId = :memberId")
    void updateFundingMember(Long fundingId, Long memberId, Integer amount);

    @Modifying
    @Query("UPDATE FundingMembers f SET f.isAutoFundingStatus = false WHERE f.fundingId = :fundingId")
    void unlinkAllByFundingId(Long fundingId);

    @Query("""
        SELECT new com.ieum.funding.response.CurrentFundingResult1DTO(
        f.fundingId,\s
        fac.facilityName,\s
        fac.facilityImage,\s
        fm.fundingTotalAmount)
        FROM FundingMembers fm\s
        JOIN Funding f ON f.fundingId = fm.fundingId\s
        JOIN Facilities fac ON f.facilityId = fac.facilityId\s
        WHERE fm.memberId = :memberId AND fm.isAutoFundingStatus = true
        """)
    CurrentFundingResult1DTO getCurrentFunding1(Long memberId);

    @Query("""
        SELECT new com.ieum.funding.response.CurrentFundingResult2DTO(COUNT(fm))
        FROM FundingMembers fm
        WHERE fm.memberId = :memberId AND fm.fundingTotalAmount > 0
        """)
    CurrentFundingResult2DTO getCurrentFunding2(Long memberId);

    @Query("""
            SELECT fm
            FROM FundingMembers fm
            WHERE fm.memberId = :memberId AND fm.isAutoFundingStatus = true
        """)
    Optional<FundingMembers> fetchFundingMembersByMemberId(Long memberId);
}
