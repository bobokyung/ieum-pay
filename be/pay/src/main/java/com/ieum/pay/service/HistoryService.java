package com.ieum.pay.service;

import com.ieum.pay.domain.*;
import com.ieum.pay.dto.HistoryDTO;
import com.ieum.pay.dto.PaymentHistoriesDTO;
import com.ieum.pay.repository.*;
import com.ieum.pay.response.FundingDonationResultResponseDTO;
import com.ieum.pay.response.HistoryResponseDTO;
import com.ieum.pay.response.PaymentHistoryResponseDTO;
import com.ieum.pay.response.RemittanceHistoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final WithdrawalHistoryRepository withdrawalHistoryRepository;
    private final DepositHistoryRepository depositHistoryRepository;
    private final ChargeHistoryRepository chargeHistoryRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final StoreRepository storeRepository;
    private final PaymoneyRepository paymoneyRepository;

    //페이머니 송금
    public Long sendMoney(Long senderId, String receiverName, int amount, int chargeMoney, Long cardId) {
        //사용자간 페이머니 송금시 출금순서
        //history
        Histories history = historySave(senderId,amount,"출금");
        //withdrawal
        withdrawalHistorySave(history.getHistoryId(), senderId,amount,receiverName);

        if(chargeMoney > 0){
            chargeHistorySave(history.getHistoryId(), senderId,cardId,chargeMoney);
        }
        return history.getHistoryId();
    }

    public void receiveMoney(Long receiverId, String senderName, int amount) {
        //사용자간 페이머니 송금시 출금순서
        Histories history = historySave(receiverId,amount,"입금");

        depositHistorySave(history.getHistoryId(), receiverId,amount,senderName);
    }

    //결제
    public Long payment(Long memberId, Long storeId, Long fundingId, Long cardId, int chargeAmount, int amount, int donationAmount) {

        //history
        Histories result = historySave(memberId,amount + donationAmount,"결제");// 0 revise check
        Long historyId = result.getHistoryId();
        //charge
        if(chargeAmount > 0){
            chargeHistorySave(historyId, memberId,cardId,chargeAmount);
        }
        //payment
        paymentHistorySave(historyId, memberId,storeId,amount,true);

        //donation
        if(donationAmount > 0){
            donationHistorySave(historyId,memberId,fundingId,donationAmount);
            Paymoney paymoney = paymoneyRepository.findByMemberId(memberId);
            paymoney.setDonationCount(paymoney.getDonationCount() + 1);
            paymoney.setDonationTotalAmount(paymoney.getDonationTotalAmount() + donationAmount);
            paymoneyRepository.save(paymoney);
        }
        return historyId;

    }
    public Histories historySave(Long memberId, int totalAmount, String historyType){
        Histories history = Histories.builder()
                .memberId(memberId)
                .totalAmount(totalAmount)
                .historyType(historyType)
                .historyDate(LocalDateTime.now())
                .build();
        return historyRepository.save(history);
    }
    public void chargeHistorySave(Long historyId, Long memberId, Long cardId, int chargeMoney){
        ChargeHistories history = ChargeHistories.builder()
                .registeredCardId(cardId)
                .historyId(historyId)
                .memberId(memberId)
                .chargeAmount(chargeMoney)
                .build();
        chargeHistoryRepository.save(history);
    }
    public void withdrawalHistorySave(Long historyId, Long memberId, int amount, String brief){
        WithdrawalHistories history = WithdrawalHistories.builder()
                .historyId(historyId)
                .memberId(memberId)
                .transactionAmount(amount)
                .withdrawalBrief(brief)
                .build();
        withdrawalHistoryRepository.save(history);
    }
    public void depositHistorySave(Long historyId, Long memberId, int amount, String brief){
        DepositHistories history = DepositHistories.builder()
                .historyId(historyId)
                .memberId(memberId)
                .transactionAmount(amount)
                .depositBrief(brief)
                .build();
        depositHistoryRepository.save(history);
    }
    public void paymentHistorySave(Long historyId, Long memberId, Long storeId, int paymentAmount, boolean payment){
        PaymentHistories history = PaymentHistories.builder()
                .historyId(historyId)
                .memberId(memberId)
                .paymentAmount(paymentAmount)
                .payment(payment) // 결제 여부
                .storeId(storeId)
                .build();
        paymentHistoryRepository.save(history);
    }

    private void donationHistorySave(Long historyId, Long memberId, Long fundingId, int amount) {
        DonationHistories history = DonationHistories.builder()
                .historyId(historyId)
                .memberId(memberId)
                .fundingId(fundingId)
                .donationAmount(amount)
                .build();
        donationHistoryRepository.save(history);
    }

    //결제 내역 조회
    public List<HistoryResponseDTO> historyList(Long memberId) {
        List<Histories> historyList = historyRepository.findByMemberIdOrderByHistoryDateDesc(memberId);

        List<WithdrawalHistories> withdrawalHistoryList = withdrawalHistoryRepository.findByMemberId(memberId);
        List<DepositHistories> depositHistoryRepositoryList = depositHistoryRepository.findByMemberId(memberId);
        List<ChargeHistories> chargeHistoryRepositoryList = chargeHistoryRepository.findByMemberId(memberId);
        List<PaymentHistoriesDTO> paymentHistoryRepositoryList = paymentHistoryRepository.findByMemberId(memberId);
        List<DonationHistories> donationHistoryRepositoryList = donationHistoryRepository.findByMemberId(memberId);

        List<HistoryResponseDTO> result = new ArrayList<>();
        HashMap<Long, Integer> map = new HashMap<>();

        int i = 0;
        for(Histories h : historyList){
            map.put(h.getHistoryId(),i++);
            HistoryResponseDTO dto = new HistoryResponseDTO(h.getHistoryId(),h.getHistoryType(),h.getTotalAmount(),h.getHistoryDate());
            result.add(dto);
        }

        for(ChargeHistories h : chargeHistoryRepositoryList){
            int index = map.get(h.getHistoryId());
            if(result.get(index).getType().equals("충전"))
                result.get(index).setTitle("이음페이머니");

            HistoryDTO dto = HistoryDTO.builder()
                    .type("충전")
                    .name("이음페이머니")
                    .price(h.getChargeAmount())
                    .build();
            result.get(index).getDetail().add(dto);
        }

        for(WithdrawalHistories h : withdrawalHistoryList){
            int index = map.get(h.getHistoryId());
            if(result.get(index).getType().equals("출금"))
                result.get(index).setTitle(h.getWithdrawalBrief());
            HistoryDTO dto = HistoryDTO.builder()
                    .type("출금")
                    .name(h.getWithdrawalBrief())
                    .price(h.getTransactionAmount())
                    .build();
            result.get(index).getDetail().add(dto);
        }

        for(DepositHistories h : depositHistoryRepositoryList){
            int index = map.get(h.getHistoryId());

            if(result.get(index).getType().equals("입금"))
                result.get(index).setTitle(h.getDepositBrief());

            HistoryDTO dto = HistoryDTO.builder()
                    .type("입금")
                    .name(h.getDepositBrief())
                    .price(h.getTransactionAmount())
                    .build();
            result.get(index).getDetail().add(dto);
        }



        for(PaymentHistoriesDTO h : paymentHistoryRepositoryList){
            int index = map.get(h.getHisid());

            if(result.get(index).getType().equals("결제"))
                result.get(index).setTitle(h.getSname());

            HistoryDTO dto = HistoryDTO.builder()
                    .type("결제")
                    .name(h.getSname())
                    .price(h.getAmount())
                    .build();
            result.get(index).getDetail().add(dto);
        }

        for(DonationHistories h : donationHistoryRepositoryList){
            int index = map.get(h.getHistoryId());

            if(result.get(index).getType().equals("기부"))
                result.get(index).setTitle("직접 기부");

            HistoryDTO dto = HistoryDTO.builder()
                    .type("기부")
                    .name(h.getFundingId().toString())
                    .price(h.getDonationAmount())
                    .build();
            result.get(index).getDetail().add(dto);
        }

        return result;
    }

    public PaymentHistoryResponseDTO getHistory(Long historyId) {
        Histories history = historyRepository.findByHistoryId(historyId);
        if(history==null || !history.getHistoryType().equals("결제"))
            return null;
        PaymentHistories paymentHistory = paymentHistoryRepository.findByHistoryId(historyId);
        DonationHistories donationHistory = donationHistoryRepository.findByHistoryId(historyId);
        Long storeId = paymentHistory.getStoreId();
        String storeName = storeRepository.findByStoreId(storeId).getStoreName();
        int payAmount = paymentHistory.getPaymentAmount();
        int donationAmount = 0;
        Long fundingId = 0L;
        if(donationHistory != null) {
            donationAmount = donationHistory.getDonationAmount();
            fundingId = donationHistory.getFundingId();
        }

        PaymentHistoryResponseDTO dto = PaymentHistoryResponseDTO.builder()
                .fundingId(fundingId)
                .storeName(storeName)
                .paymentAmount(payAmount)
                .paymentAmount(payAmount)
                .donationAmount(donationAmount)
                .build();

        return dto;
    }

    //직접 기부
    public Long directDonation(Long memberId, Long fundingId, int donationAmount, Long cardId) {
        Histories history = historySave(memberId, donationAmount, "기부");
        Paymoney paymoney = paymoneyRepository.findByMemberId(memberId);
        int updatePaymoney = paymoney.getPaymoneyAmount();
        if(paymoney.getPaymoneyAmount() < donationAmount){
            int chargeAmount = (int) Math.ceil((double) (donationAmount - paymoney.getPaymoneyAmount()) / 10000) * 10000;
            chargeHistorySave(history.getHistoryId(),memberId,cardId,chargeAmount);
            updatePaymoney += chargeAmount;
        }
        donationHistorySave(history.getHistoryId(), memberId,fundingId, donationAmount);
        paymoney.setPaymoneyAmount(updatePaymoney - donationAmount);
        paymoney.setDonationCount(paymoney.getDonationCount() + 1);
        paymoney.setDonationTotalAmount(paymoney.getDonationTotalAmount() + donationAmount);
        paymoneyRepository.save(paymoney);
        return history.getHistoryId();
    }

    //기부 완료 정보 요청
    public FundingDonationResultResponseDTO getFundingHistory(Long historyId) {
        Histories history = historyRepository.findByHistoryId(historyId);

        if(history==null ||  history.getHistoryType().equals("출금") || history.getHistoryType().equals("입금"))
            return null;

        DonationHistories donationHistory = donationHistoryRepository.findByHistoryId(historyId);

        if(donationHistory==null)
            return null;

        FundingDonationResultResponseDTO dto = FundingDonationResultResponseDTO.builder()
                .fundingId(donationHistory.getFundingId())
                .donationAmount(donationHistory.getDonationAmount())
                .historyDate(history.getHistoryDate())
                .build();

        return dto;
    }

    public DonationHistories getDonationHistory(Long memberId, Long historyId) {
        return donationHistoryRepository.findByHistoryIdAndMemberId(historyId,memberId);
    }

    public Histories getHistoryEntity(Long historyId){
        return historyRepository.findByHistoryId(historyId);
    }

    public RemittanceHistoryResponseDTO getRemmitanceHistory(Long memberId, Long historyId) {
        Histories history = historyRepository.findByHistoryIdAndMemberId(historyId, memberId);
        if(history==null || !history.getHistoryType().equals("출금"))
            return null;
        WithdrawalHistories  withdrawalHistories = withdrawalHistoryRepository.findByHistoryId(historyId);


        RemittanceHistoryResponseDTO dto = RemittanceHistoryResponseDTO.builder()
                .historyId(historyId)
                .name(withdrawalHistories.getWithdrawalBrief())
                .amount(withdrawalHistories.getTransactionAmount())
                .build();

        return dto;
    }
}
