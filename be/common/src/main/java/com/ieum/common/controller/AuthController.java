package com.ieum.common.controller;

import static com.ieum.common.format.code.FailedCode.AUTHENTICATION_2FA_FAILED;

import com.google.gson.Gson;
import com.ieum.common.annotation.CurrentMemberId;
import com.ieum.common.dto.request.paymentPasswordRequestDTO;
import com.ieum.common.dto.request.secondaryAuthRequestDTO;
import com.ieum.common.dto.response.secondaryAuthResponseDTO;
import com.ieum.common.dto.token.FcmTokenRequestDto;
import com.ieum.common.format.code.SuccessCode;
import com.ieum.common.format.code.Topic;
import com.ieum.common.format.response.ResponseTemplate;
import com.ieum.common.message.FcmConnectionRequestMessage;
import com.ieum.common.service.AuthService;
import com.ieum.common.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "인증 API - 목업")
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Gson gson;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ResponseTemplate response;
    private final StringRedisTemplate stringRedisTemplate;
    private final AuthService authService;
    private final TokenService tokenService;

    @Operation(summary = "액세스 토큰 갱신", description = "만료된 액세스 토큰을 새롭게 갱신")
    @ApiResponse(responseCode = "200", description = "새로운 액세스 토큰 반환")
    @PutMapping("/token-renew")
    public ResponseEntity<?> renewAccessToken(
        HttpServletRequest servletRequest
    ) {
        String redIssueAccessToken = tokenService.reIssueAccessToken(servletRequest);
        return response.success(redIssueAccessToken, SuccessCode.ACCESS_TOKEN_RENEWED);
    }

    @Operation(summary = "본인 확인용 인증 메시지 발급", description = "본인 확인 메시지 요청을 받아 인증 코드를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "본인 확인 메시지용 코드 반환")
    @GetMapping
    public ResponseEntity<?> getAuthenticationCode(
        @RequestParam("phone-number")
        @NotBlank(message = "휴대폰 번호는 필수입니다.")
        @Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호 형식이 잘못되었습니다.")
        String phoneNumber
    ) {
        String code = "code:" + UUID.randomUUID() + "%";
        stringRedisTemplate.opsForValue().set("phone-number:" + phoneNumber, code, 5, TimeUnit.MINUTES);
        return response.success(code, SuccessCode.AUTHENTICATION_CODE_ISSUED);
    }

    @Operation(summary = "2차 비밀번호 인증", description = "2차 비밀번호 인증 요청")
    @ApiResponse(responseCode = "200", description = "인증키, 성공여부 반환")
    @PostMapping("/password")
    public ResponseEntity<?> secondaryAuthentication(
        @RequestBody paymentPasswordRequestDTO req,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        secondaryAuthRequestDTO reqDto = secondaryAuthRequestDTO.builder()
                                                                .paymentPassword(req.getPaymentPassword())
                                                                .memberId(memberId)
                                                                .build();
        secondaryAuthResponseDTO res = authService.secondaryAuthentication(reqDto);
        if (res.getAuth()) {
            return response.success(res, SuccessCode.AUTHENTICATION_CODE_ISSUED);
        }
        return response.fail(AUTHENTICATION_2FA_FAILED.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "FCM 토큰 등록", description = "로그인 후 클라이언트로부터 받은 FCM 토큰을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "FCM 토큰 등록 성공")
    @PostMapping("/register-fcm-token")
    public ResponseEntity<?> registerFcmToken(
        @RequestBody FcmTokenRequestDto fcmTokenRequestDTO,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        kafkaTemplate.send(Topic.FCM_CONNECT.getTopicName(),
                           gson.toJson(FcmConnectionRequestMessage.builder()
                                                                  .fcmToken(fcmTokenRequestDTO.getFcmToken())
                                                                  .memberId(memberId)
                                                                  .build()));
        return response.success(SuccessCode.SUCCESS);
    }
}
