package com.ieum.common.service;

import com.ieum.common.dto.feign.funding.request.FundingDonationRequestDTO;
import com.ieum.common.dto.feign.funding.response.AutoFundingResultResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingInfoResponseDTO;
import com.ieum.common.dto.request.PaymentRequestDTO;
import com.ieum.common.dto.response.PaymentInfoResponseDTO;
import com.ieum.common.dto.response.PaymentResponseDTO;
import com.ieum.common.feign.FundingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayService payService;
    private final FundingFeignClient fundingFeignClient;


    public PaymentResponseDTO processPayment(Long memberId, PaymentRequestDTO dto) {
        //연동 확인
        FundingInfoResponseDTO funding = fundingFeignClient.getAutoFundingInfo(memberId);
//        AutoFundingResultResponseDTO fund =  fundingFeignClient.donationAuto(FundingDonationRequestDTO.builder()
//                        .memberId()
//                        .amount()
//                .build());
//        payService.payment(memberId,dto.getStoreId(),,dto.g)
        return PaymentResponseDTO.builder().build();
    }

    public void verifyPaymentPassword() {

    }

    public void updatePaymentPassword() {

    }

    public void getPaymentHistory() {

    }

    public PaymentInfoResponseDTO getPaymentInfo(Long memberId, Long storeId, int price) {
        FundingInfoResponseDTO funding = fundingFeignClient.getAutoFundingInfo(memberId);
        //기부 가능한 총액
        String storeName = payService.getStoreName(storeId);
        int nowPaymoney = payService.nowMyPaymoney(memberId);
        int chargeMoney = 0;
        if(price > nowPaymoney){
            chargeMoney = (price - nowPaymoney) / 10000;
            if(price - nowPaymoney > chargeMoney)
                chargeMoney += 10000;
        }

        int donationMoney = 0;
        String facilityName = "";
        if(funding != null){
            donationMoney = (chargeMoney + nowPaymoney - price) % 1000;

            if(donationMoney > funding.getAmount()){
                donationMoney = funding.getAmount();
            }
            facilityName = funding.getFacilityName();
        }
        return PaymentInfoResponseDTO.builder()
                .storeId(storeId)
                .price(price)
                //.cardNickname()
                .storeName(storeName)
                .paymoneyAmount(nowPaymoney)
                .chargeAmount(chargeMoney)
                .donationMoney(donationMoney)
                .facilityName(facilityName)
                .build();
    }
}