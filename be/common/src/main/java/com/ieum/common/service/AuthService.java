package com.ieum.common.service;

import com.ieum.common.dto.request.secondaryAuthRequestDTO;
import com.ieum.common.dto.response.secondaryAuthResponseDTO;
import com.ieum.common.feign.PayFeignClient;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PayFeignClient payFeignClient;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    // 2차 비밀번호 인증 후 레디스에 키 저장
    public secondaryAuthResponseDTO secondaryAuthentication(secondaryAuthRequestDTO req) {
        String getPassword = payFeignClient.getPaymentPassword(req.getMemberId());
        boolean checkPassword = passwordEncoder.matches(req.getPaymentPassword(), getPassword);
        if (checkPassword) {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[12];
            random.nextBytes(bytes);

            // 바이트 배열을 Base64 인코딩하여 랜덤 문자열 생성
            String authKey = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

            stringRedisTemplate.opsForValue().set("2ndAuth:" + req.getMemberId(),
                                                  authKey, 3, TimeUnit.MINUTES);

            return secondaryAuthResponseDTO.builder()
                                           .auth(true)
                                           .authenticationKey(authKey)
                                           .build();
        }
        return secondaryAuthResponseDTO.builder()
                                       .auth(false)
                                       .build();
    }

    // 2차 비밀번호 확인 인증 - 레디스에 키가 있는지
    public boolean checkAuthInRedis(Long memberId, String authKey) {
        String getAuthKeyFromRedis = stringRedisTemplate.opsForValue().get("2ndAuth:" + memberId);
        return getAuthKeyFromRedis != null && getAuthKeyFromRedis.equals(authKey);
    }

}
