package com.ieum.funding.repository;

import com.ieum.funding.domain.Funding;
import com.ieum.funding.dto.FundingDetailBaseDTO;
import com.ieum.funding.dto.FundingInfoDTO;
import com.ieum.funding.response.FundingInfoResponseDTO;
import com.ieum.funding.response.FundingReceiptResponseFromFDTO;
import com.ieum.funding.response.FundingResultResponseDTO;
import com.ieum.funding.response.FundingSummaryResponseDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface FundingRepository extends JpaRepository<Funding, Long> {

    String BASE_QUERY_INFO_LIST = "SELECT new com.ieum.funding.response.FundingSummaryResponseDTO(" +
        "f.fundingId, " +
        "fac.facilityName, " +
        "f.fundingTitle, " +
        "f.fundingOpenDate, " +
        "f.fundingFinishDate, " +
        "fac.facilityImage, " +
        "(SELECT COUNT(fm) as cnt FROM FundingMembers fm WHERE fm.fundingId = f.fundingId AND fm.fundingTotalAmount > 0 ), " +
        "f.goalAmount, " +
        "f.currentAmount) " +
        "FROM Funding f " +
        "LEFT JOIN Facilities fac ON fac.facilityId = f.facilityId ";

    @Query(BASE_QUERY_INFO_LIST + "WHERE f.fundingFinishDate IS NULL")
    List<FundingSummaryResponseDTO> findOngoingFundingList();

    @Query(BASE_QUERY_INFO_LIST + "WHERE f.fundingFinishDate IS NOT NULL")
    List<FundingSummaryResponseDTO> findCompleteFundingList();

    @Query("SELECT new com.ieum.funding.dto.FundingDetailBaseDTO(" +
        "f.fundingId, " +
        "fac.facilityName, " +
        "fac.facilityAddress, " +
        "fac.facilityPhoneNumber, " +
        "fac.facilityRepresentativeName, " +
        "fac.facilityRepresentativePhoneNumber, " +
        "fac.facilityCapacity, " +
        "fac.facilityImage, " +
        "f.fundingOpenDate, " +
        "f.fundingFinishDate, " +
        "(SELECT COUNT(fm) as cnt FROM FundingMembers fm WHERE fm.fundingId = f.fundingId), " +
        "f.fundingTitle, " +
        "f.goalAmount, " +
        "f.currentAmount," +
        "f.fundingContent) " +
        "FROM Funding f " +
        "LEFT JOIN Facilities fac ON fac.facilityId = f.facilityId " +
        "WHERE f.fundingId = :fundingId")
    FundingDetailBaseDTO findFundingDetail(@Param("fundingId") Long fundingId);

    @Query("SELECT new com.ieum.funding.dto.FundingInfoDTO(f.fundingId," +
        " f.goalAmount - f.currentAmount, " +
        "(SELECT facilityName from Facilities " +
        "WHERE facilityId = f.facilityId)) " +
        "FROM Funding f " +
        "WHERE f.fundingId = :fundingId")
    FundingInfoDTO getDonationInfo(Long fundingId);

    @Modifying
    @Transactional
    @Query("UPDATE Funding f SET f.currentAmount = f.currentAmount + :amount WHERE f.fundingId = :fundingId")
    void updateFunding(Long fundingId, Integer amount);

    Funding findByFundingId(Long fundingId);

    @Transactional
    @Modifying
    @Query("UPDATE Funding f SET f.fundingFinishDate = CURRENT_TIMESTAMP WHERE f.fundingId = :fundingId AND f.currentAmount = f.goalAmount")
    void updateFinishDate(Long fundingId);

    @Query("SELECT new com.ieum.funding.response.FundingResultResponseDTO(f.fundingTitle, " +
        "fac.facilityName, " +
        "fac.facilityImage )" +
        "FROM Funding f " +
        "JOIN Facilities fac ON f.facilityId = fac.facilityId " +
        "WHERE f.fundingId = :fundingId")
    FundingResultResponseDTO getFacilityInfo(Long fundingId);


    @Query("SELECT new com.ieum.funding.response.FundingInfoResponseDTO(" +
            "f.fundingId, " +
            "(f.goalAmount - f.currentAmount), " +
            "fac.facilityName) " +
            "FROM Funding f " +
            "JOIN Facilities fac ON f.facilityId = fac.facilityId " +
            "WHERE f.fundingId IN (SELECT fm.fundingId FROM FundingMembers fm WHERE fm.memberId = :memberId AND fm.isAutoFundingStatus = true)"
    )
    FundingInfoResponseDTO getAutoDonationInfo(Long memberId);

    @Query("SELECT new com.ieum.funding.response.FundingReceiptResponseFromFDTO(" +
        "fac.facilityName, " +
        "f.fundingTitle) " +
        "FROM Funding f " +
        "JOIN Facilities fac ON f.facilityId = fac.facilityId " +
        "WHERE f.fundingId = :fundingId ")
    FundingReceiptResponseFromFDTO getReceiptInfo(Long fundingId);

    @Query(BASE_QUERY_INFO_LIST +
        " WHERE f.fundingId in ( SELECT fmm.fundingId FROM FundingMembers fmm " +
        " WHERE fmm.memberId = :memberId  And fmm.fundingTotalAmount > 0 " +
        ") "
    )
    List<FundingSummaryResponseDTO> findParticipantFundingList(Long memberId);

}
