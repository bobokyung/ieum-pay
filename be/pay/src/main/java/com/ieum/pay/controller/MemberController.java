package com.ieum.pay.controller;

import com.ieum.pay.request.MemberPayPasswordRequestDTO;
import com.ieum.pay.request.MyCardCheckRequestDTO;
import com.ieum.pay.request.RegisterRequestDTO;
import com.ieum.pay.service.PaymoneyService;
import com.ieum.pay.service.CardRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final PaymoneyService paymoneyService;
    private final CardRegistrationService cardRegistrationService;

    @PostMapping("/register")
    public boolean signUp(@RequestBody RegisterRequestDTO requestDTO){
        return paymoneyService.signMember(requestDTO.getMemberId(), requestDTO.getPaymentPassword());
    }

    @PutMapping("pay-pw")
    public boolean updatePayPassword(@RequestBody MemberPayPasswordRequestDTO requestDTO){
        return paymoneyService.updatePayPassword(requestDTO.getMemberId(),requestDTO.getPaymentPassword());
    }

    @PostMapping("/mine")
    public boolean isMine(@RequestBody MyCardCheckRequestDTO requestDTO){
        return cardRegistrationService.isMyCard(requestDTO.getMemberId(), requestDTO.getRegisteredCardId());
    }

    @GetMapping("/pp/{memberId}")
    public String getPaymentPassword(@PathVariable Long memberId){
        return paymoneyService.getPp(memberId);
    }
}
