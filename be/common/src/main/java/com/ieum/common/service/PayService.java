package com.ieum.common.service;

import com.google.gson.Gson;
import com.ieum.common.domain.Members;
import com.ieum.common.dto.feign.pay.request.*;
import com.ieum.common.dto.feign.pay.response.*;
import com.ieum.common.dto.request.PayRemittancePaymoneyRequestDTO;
import com.ieum.common.dto.response.CardOcrResponseDTO;
import com.ieum.common.exception.member.MemberNotFoundByIdException;
import com.ieum.common.exception.pay.PayCardNotFoundException;
import com.ieum.common.feign.PayFeignClient;
import com.ieum.common.format.code.Topic;
import com.ieum.common.message.TransferReceivedMessage;
import com.ieum.common.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PayService {

    private final Gson gson;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PayFeignClient payFeignClient;
    private final MemberRepository memberRepository;

    public CardOcrResponseDTO getOcr(MultipartFile img) {
        return payFeignClient.getCardOcrResult(img);
    }

    public Long cardRegister(Long memberId, String cardNumber, String cardNickname) {
        return payFeignClient.isCardNumberValid(CardRegisterRequestDTO.builder()
                                                                      .memberId(memberId)
                                                                      .cardNumber(cardNumber)
                                                                      .cardNickname(cardNickname)
                                                                      .build());
    }

    public Boolean updateCard(Long memberId, Long registeredCardId) {
        return payFeignClient.deleteCard(registeredCardId, memberId);
    }

    public MainSummaryResponseDTO getMainPageInfo(Long memberId) {
        return payFeignClient.getMainPageInfo(memberId);
    }

    public boolean createPaymoney(Long memberId, String payPassword) {
        return payFeignClient.createPayMoneyRegister(RegisterRequestDTO.builder()
                                                                       .memberId(memberId)
                                                                       .paymentPassword(payPassword)
                                                                       .build());
    }

    public boolean updatePayPassword(Long memberId, String paymentPassword) {
        return payFeignClient.updatePayPassword(MemberPayPasswordRequestDTO.builder()
                                                                           .memberId(memberId)
                                                                           .paymentPassword(paymentPassword)
                                                                           .build());
    }

    public FundingDonationResultResponseDTO getDonationHistory(Long historyId) {
        return payFeignClient.getDonationHistory(historyId).orElseThrow();
    }

    public Long directDonation(Long memberId, Long fundingId, int donationAmount, Long cardId) {
        return payFeignClient.directDonation(FundingDonationRequestDTO.builder()
                                                                      .memberId(memberId)
                                                                      .fundingId(fundingId)
                                                                      .donationAmount(donationAmount)
                                                                      .cardId(cardId)
                                                                      .build());
    }

    public List<HistoryResponseDTO> getHistoryList(Long memberId) {
        return payFeignClient.history(memberId);
    }

    public int HowManyCharge(Long memberId, int tradeAmount) {
        return payFeignClient.chargeMoney(PaymentChargeMoney.builder()
                                                            .memberId(memberId)
                                                            .paymentMoney(tradeAmount)
                                                            .build());
    }

    public Long payment(Long memberId, Long storeId, Long fundingId, Long cardId,
                        int amount, int donationAmount, int chargeAmount) {

        return payFeignClient.payment(PaymentRequestDTO.builder()
                                                       .memberId(memberId)
                                                       .storeId(storeId)
                                                       .fundingId(fundingId)
                                                       .cardId(cardId)
                                                       .amount(amount)
                                                       .donationAmount(donationAmount)
                                                       .chargeAmount(chargeAmount)
                                                       .build());
    }

    public String getStoreName(Long storeId) {
        return payFeignClient.getStoreName(storeId);
    }

    public PaymentHistoryPayResponseDTO getPaymentHistory(Long memberId, Long historyId) {
        return payFeignClient.getPaymentHistory(historyId);
    }

    public int nowMyPaymoney(Long memberId) {
        return payFeignClient.myPaymoney(memberId);
    }

    public Long remmitanceAccount(Long memberId, String memberName, String receiverName, int amount, Long cardId) {
        return payFeignClient.remittanceAccount(RemittanceAccountRequestDTO.builder()
                                                                           .senderId(memberId)
                                                                           .senderName(memberName)
                                                                           .receiverName(receiverName)
                                                                           .amount(amount)
                                                                           .cardId(cardId)
                                                                           .build());
    }

    public DonationReceiptResponseDTO getDonationReceiptInfo(Long memberId, Long historyId) {
        return payFeignClient.donationReceipt(memberId, historyId);
    }

    public boolean checkMyCard(Long memberId, Long registeredCardId) {
        return payFeignClient.isMine(MyCardCheckRequestDTO.builder()
                                                          .memberId(memberId)
                                                          .registeredCardId(registeredCardId)
                                                          .build());
    }

    public RemittanceHistoryResponseDTO getRemittanceHistory(Long memberId, Long historyId) {
        return payFeignClient.getRemittanceHistory(memberId, historyId);
    }

    public Optional<String> getCardName(Long registeredCardId) {
        return payFeignClient.getCardName(registeredCardId);
    }

    public CardDetailResponseDTO getCardDetail(String cardNum) {
        return payFeignClient.getCardCompany(cardNum);
    }

    public Long remmitancePaymoney(Long senderId, PayRemittancePaymoneyRequestDTO request) {

        Members sender = memberRepository.findById(senderId).orElseThrow(MemberNotFoundByIdException::new);
        if (sender.getPaycardId() == null) {
            throw new PayCardNotFoundException();
        }

        Members receiver = memberRepository.findByPhoneNumber(request.getPhoneNumber());
        Long result = payFeignClient.remittancePaymoney(getRemittanceRequestDTO(senderId, request, sender, receiver));

        kafkaTemplate.send(Topic.TRANSFER_RECEIVED.getTopicName(),
                           gson.toJson(getTransferReceivedMessage(request, receiver, sender, sender.getName())));

        return result;
    }


    private static TransferReceivedMessage getTransferReceivedMessage(PayRemittancePaymoneyRequestDTO request,
                                                                      Members receiver, Members sender, String senderName) {
        return TransferReceivedMessage.builder()
                                      .amount(request.getAmount())
                                      .receiverId(receiver.getId())
                                      .senderId(sender.getId())
                                      .senderName(senderName)
                                      .build();
    }

    private static RemittanceRequestDTO getRemittanceRequestDTO(Long senderId, PayRemittancePaymoneyRequestDTO request,
                                                                Members sender, Members receiver) {
        return RemittanceRequestDTO.builder()
                                   .senderId(senderId)
                                   .senderName(sender.getName())
                                   .receiverId(receiver.getId())
                                   .receiverName(receiver.getName())
                                   .amount(request.getAmount())
                                   .cardId(sender.getPaycardId())
                                   .build();
    }
}
