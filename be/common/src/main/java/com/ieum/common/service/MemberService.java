package com.ieum.common.service;

import com.ieum.common.domain.Grade;
import com.ieum.common.domain.Members;
import com.ieum.common.domain.Paymoney;
import com.ieum.common.dto.feign.funding.response.CurrentFundingResultResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingInfoResponseDTO;
import com.ieum.common.dto.feign.funding.response.FundingResultResponseDTO;
import com.ieum.common.dto.feign.pay.dto.CardDTO;
import com.ieum.common.dto.feign.pay.dto.HistoryDTO;
import com.ieum.common.dto.feign.pay.response.HistoryResponseDTO;
import com.ieum.common.dto.feign.pay.response.MainSummaryResponseDTO;
import com.ieum.common.dto.member.req.LoginRequestDto;
import com.ieum.common.dto.member.req.NicknameUpdateRequestDto;
import com.ieum.common.dto.member.req.PasswordUpdateRequestDto;
import com.ieum.common.dto.member.req.SignupRequestDto;
import com.ieum.common.dto.member.res.ProfileResponseDto;
import com.ieum.common.dto.member.res.RecipientResponseDto;
import com.ieum.common.dto.member.res.UpdatedNicknameResponseDto;
import com.ieum.common.dto.response.MainResponseDTO;
import com.ieum.common.dto.response.MemberSummaryResponseDTO;
import com.ieum.common.dto.token.TokenInfo;
import com.ieum.common.exception.pay.PayMoneyCreationFailedException;
import com.ieum.common.exception.feign.PaymentServiceUnavailableException;
import com.ieum.common.exception.member.ExistingPhoneNumberException;
import com.ieum.common.exception.member.InvalidLoginAttemptException;
import com.ieum.common.exception.member.InvalidPhoneNumberException;
import com.ieum.common.exception.member.MemberNotFoundByIdException;
import com.ieum.common.exception.member.MemberNotFoundByPhoneNumberException;
import com.ieum.common.exception.member.MemberNotFoundException;
import com.ieum.common.exception.member.PasswordMismatchException;
import com.ieum.common.feign.FundingFeignClient;
import com.ieum.common.jwt.TokenProvider;
import com.ieum.common.repository.GradeRepository;
import com.ieum.common.repository.MemberRepository;
import com.ieum.common.repository.PaymoneyRepository;
import com.ieum.common.util.CookieUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    public static final String REFRESH_TOKEN = "RefreshToken";
    private final CookieUtil cookieUtil;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate stringRedisTemplate;

    private final PayService payService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final GradeRepository gradeRepository;
    private final PaymoneyRepository paymoneyRepository;
    private final FundingFeignClient fundingFeignClient;


    /**
     * 회원 가입을 처리하고, 관련 페이머니 계정을 생성합니다.
     *
     * <p>이 메서드는 다음과 같은 순서로 작업을 수행합니다:
     * <ol>
     *     <li>입력된 비밀번호와 비밀번호 확인이 일치하는지 검사합니다.</li>
     *     <li>전화번호의 중복 여부를 검사합니다.</li>
     *     <li>전화번호 인증 상태를 확인합니다.</li>
     *     <li>검사를 통과하면 주어진 정보로 새로운 회원 객체를 생성하고 데이터베이스에 저장합니다.</li>
     *     <li>회원 저장이 성공하면, 해당 회원의 ID를 사용하여 페이머니 계정을 생성합니다.</li>
     *     <li>페이머니 계정 생성이 실패하면 {@link PayMoneyCreationFailedException}을 발생시킵니다.</li>
     *     <li>페이머니 계정 생성에 관한 외부 서비스 호출이 실패하면 {@link PaymentServiceUnavailableException}을 발생시킵니다.</li>
     * </ol>
     * </p>
     *
     * @param request 회원 가입 요청 정보를 담고 있는 DTO. 회원의 전화번호, 이름, 닉네임, 비밀번호, 비밀번호 확인, 결제 비밀번호를 포함합니다.
     * @return 생성된 회원의 ID
     * @throws InvalidPhoneNumberException        전화번호 인증이 완료되지 않았을 때 발생
     * @throws PayMoneyCreationFailedException    페이머니 계정 생성이 실패했을 때 발생
     * @throws PaymentServiceUnavailableException 페이머니 계정 생성에 필요한 외부 서비스 호출이 실패했을 때 발생
     */
    public Long signup(SignupRequestDto request) {

        try {
            validateCredentials(request);
        } catch (PasswordMismatchException | ExistingPhoneNumberException | InvalidPhoneNumberException e) {
            throw e;
        }

        Grade grade = gradeRepository.findByCode("GR001");
        Members savedMember = memberRepository.save(
            Members.of(request.getPhoneNumber(),
                       request.getName(),
                       request.getNickname(),
                       passwordEncoder.encode(request.getPassword()),
                       grade));

        paymoneyRepository.save(Paymoney.builder()
                                        .memberId(savedMember.getId())
                                        .paymentPassword(passwordEncoder.encode(request.getPaymentPassword()))
                                        .paymoneyAmount(0)
                                        .donationTotalAmount(0)
                                        .donationCount(0)
                                        .build());

        grade.increaseCount();
        return savedMember.getId();
    }

    private void validateCredentials(SignupRequestDto request) {
        CompletableFuture<Void> passwordValidation = CompletableFuture.runAsync(() -> {
            if (!request.getPassword().equals(request.getPasswordConfirm())) {
                throw new PasswordMismatchException();
            }
        });

        CompletableFuture<Void> phoneNumberDuplicationValidation = CompletableFuture.runAsync(() -> {
            validatePhoneNumberDuplication(request.getPhoneNumber());
        });

        CompletableFuture<Void> phoneNumberConfirmationValidation = CompletableFuture.runAsync(() -> {
            validatePhoneNumberConfirmation(request.getPhoneNumber());
        });

        try {
            CompletableFuture.allOf(
                passwordValidation,
                phoneNumberDuplicationValidation,
                phoneNumberConfirmationValidation).join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof PasswordMismatchException) {
                throw (PasswordMismatchException) cause;
            } else if (cause instanceof ExistingPhoneNumberException) {
                throw (ExistingPhoneNumberException) cause;
            } else if (cause instanceof InvalidPhoneNumberException) {
                throw (InvalidPhoneNumberException) cause;
            } else {
                throw e;
            }
        }
    }


    /**
     * 전화번호로 회원 존재 여부를 확인합니다.
     *
     * @param phoneNumber 확인할 회원의 전화번호입니다.
     * @return 회원이 존재하면 true, 그렇지 않으면 false 를 반환합니다.
     */
    @Transactional(readOnly = true)
    public boolean existsMemberByPhoneNumber(String phoneNumber) {
        return memberRepository.fetchMemberByPhoneNumber(phoneNumber).isPresent();
    }


    /**
     * 주어진 회원 ID에 해당하는 프로필을 조회합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 조회된 회원의 프로필 정보를 담은 {@link ProfileResponseDto}
     * @throws MemberNotFoundByIdException 주어진 ID에 해당하는 회원이 없을 경우 발생
     */
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfileById(Long memberId) {
        return memberRepository.fetchProfileById(memberId)
                               .orElseThrow(MemberNotFoundByIdException::new);
    }

    @Transactional(readOnly = true)
    public RecipientResponseDto getRecipientByPhoneNumber(String phoneNumber) {
        return memberRepository.fetchRecipientByPhoneNumber(phoneNumber)
                               .orElseThrow(MemberNotFoundByPhoneNumberException::new);
    }

    /**
     * 주어진 회원 ID에 해당하는 회원을 삭제합니다.
     *
     * @param memberId 삭제할 회원의 ID
     * @param req
     * @param res
     * @return 삭제된 회원의 ID
     */
    public Long delete(Long memberId, HttpServletRequest req, HttpServletResponse res) {
        try {
            memberRepository.deleteById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundByIdException();
        }
        logout(req, res);
        return memberId;
    }


    /**
     * 회원의 닉네임을 업데이트합니다.
     *
     * @param request  닉네임 업데이트 요청 정보를 담은 {@link NicknameUpdateRequestDto}
     * @param memberId 업데이트할 회원의 ID
     * @return 업데이트 이후의 닉네임 정보를 담은 {@link UpdatedNicknameResponseDto}
     */
    public UpdatedNicknameResponseDto updateNickname(NicknameUpdateRequestDto request, Long memberId) {
        Members findMember = findMemberById(memberId);
        String prevNickname = findMember.updateNickname(request.getNickname());
        return UpdatedNicknameResponseDto.builder()
                                         .prevNickname(prevNickname)
                                         .updatedNickname(request.getNickname())
                                         .build();
    }


    /**
     * 회원의 비밀번호를 업데이트합니다.
     *
     * @param request  비밀번호 업데이트 요청 정보를 담은 {@link PasswordUpdateRequestDto}
     * @param memberId 업데이트할 회원의 ID
     * @throws PasswordMismatchException 기존 비밀번호 확인이 실패한 경우 발생
     */
    public void updatePassword(PasswordUpdateRequestDto request, Long memberId) {
        Members findMember = findMemberById(memberId);
        validatePasswordConfirmation(request.getPrevPassword(), findMember.getPassword());
        findMember.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }


    /**
     * 주어진 회원 ID를 사용하여 회원을 찾습니다.
     *
     * @param memberId 찾을 회원의 ID
     * @return 찾아진 회원 정보를 담은 {@link Members}
     * @throws MemberNotFoundByIdException 주어진 ID에 해당하는 회원이 없을 경우 발생
     */
    public Members findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundByIdException::new);
    }


    /**
     * 사용자 로그인을 처리하고, 액세스 토큰을 반환합니다.
     * <p>
     * 로그인 요청 정보를 기반으로 사용자 인증을 수행하며, 성공 시 JWT 액세스 토큰과 리프레시 토큰을 생성합니다. 리프레시 토큰은 HTTP 쿠키에 저장됩니다.
     *
     * @param request         로그인 요청 데이터를 담고 있는 {@link LoginRequestDto} 객체
     * @param servletResponse HttpServletResponse 객체, 쿠키를 추가하기 위해 사용됨
     * @return 생성된 JWT 액세스 토큰 문자열
     * @throws MemberNotFoundException      전화번호를 통한 회원 조회 실패 시 던져짐
     * @throws InvalidLoginAttemptException 비밀번호 불일치 시 던져짐
     */
    @Transactional(readOnly = true)
    public String login(LoginRequestDto request, HttpServletResponse servletResponse) {
        Members findMember = memberRepository.fetchMemberByPhoneNumber(request.getPhoneNumber())
                                             .orElseThrow(MemberNotFoundException::new);
        validatePasswordConfirmation(request.getPassword(), findMember.getPassword());
        TokenInfo tokenInfo = tokenProvider.generateTokenInfo(findMember);
        tokenService.save(tokenInfo);
        cookieUtil.addCookie(REFRESH_TOKEN,
                             tokenInfo.getRefreshToken(),
                             tokenProvider.getREFRESH_TOKEN_TIME(),
                             servletResponse);
        return tokenInfo.getAccessToken();
    }


    /**
     * 클라이언트의 로그아웃 요청을 처리합니다.
     * <p>
     * HTTP 요청에서 refresh 토큰을 추출하여 삭제하고, 관련 쿠키를 HTTP 응답에서 제거합니다. 현재 구현에서는 항상 로그아웃이 성공적으로 처리되었음을 나타내는 true를 반환합니다.
     *
     * @param request         클라이언트로부터 받은 {@link HttpServletRequest} 객체
     * @param servletResponse 클라이언트에게 보낼 {@link HttpServletResponse} 객체
     * @return 로그아웃 프로세스가 성공적으로 완료되었는지 여부 (현재 구현에서는 항상 true)
     */
    public boolean logout(HttpServletRequest request, HttpServletResponse servletResponse) {
        tokenService.remove(cookieUtil.getRefreshTokenValue(request));
        cookieUtil.removeCookie(REFRESH_TOKEN, servletResponse);
        return true;
    }


    private void validatePasswordConfirmation(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordMismatchException();
        }
    }


    /**
     * 전화번호의 중복을 검증하는 메서드입니다. 이 메서드는 회원 가입 과정에서 입력된 전화번호가 이미 시스템에 등록된 경우를 확인하는 데 사용됩니다.
     *
     * @param phoneNumber 검증하고자 하는 전화번호입니다.
     * @throws ExistingPhoneNumberException 입력된 전화번호가 이미 사용 중일 경우 발생하는 예외입니다.
     *
     *                                      <p>전화번호 중복 검사는 회원 데이터베이스에서 입력된 전화번호를 가진 회원이 있는지 확인함으로써 이루어집니다.
     *                                      만약 해당 전화번호를 가진 회원이 이미 존재한다면, ExistingPhoneNumberException을 발생시켜 이를 호출한 메서드에 알립니다.</p>
     */
    private void validatePhoneNumberDuplication(String phoneNumber) {
        memberRepository.fetchMemberByPhoneNumber(phoneNumber)
                        .ifPresent(m -> {
                            throw new ExistingPhoneNumberException();
                        });
    }


    /**
     * 전달받은 전화번호가 확인되었는지 검증합니다. 전화번호의 확인 여부는 Redis 에 저장된 값을 통해 확인됩니다. 확인되지 않은 전화번호일 경우 {@link InvalidPhoneNumberException}을 발생시킵니다.
     *
     * @param phoneNumber 검증하고자 하는 전화번호. 이 전화번호는 Redis 에서 확인된 전화번호 목록과 대조됩니다.
     * @throws InvalidPhoneNumberException 전달받은 전화번호가 확인되지 않았을 때 발생합니다.
     */
    private void validatePhoneNumberConfirmation(String phoneNumber) {
        boolean confirmed = isPhoneNumberConfirmed(phoneNumber);
        if (!confirmed) { // 지우지 말것 이메일 서버 고치면 다시 풀어야함
            throw new InvalidPhoneNumberException();
        }
    }

    /**
     * 주어진 전화번호가 Redis 에 저장되어 있는지 확인하여, 저장되어 있다면 해당 전화번호가 확인되었음을 의미합니다. Redis 내부에서는 전화번호를 키로 하여 boolean 값을 저장하고 있으며, 이 메서드는 해당 키에 대한 값을 조회하여 전화번호의 확인
     * 여부를 반환합니다.
     *
     * @param phoneNumber 확인 여부를 조회하고자 하는 전화번호입니다. 전화번호는 Redis의 키 형태로 변환되어 조회됩니다.
     * @return 전화번호가 확인된 경우 {@code true}, 그렇지 않은 경우 {@code false}를 반환합니다.
     */
    private boolean isPhoneNumberConfirmed(String phoneNumber) {
        String redisKey = "confirmed-phone-number:" + phoneNumber;
        String confirmedValue = stringRedisTemplate.opsForValue().get(redisKey);
        return Boolean.parseBoolean(confirmedValue);
        // return true; // 시연용 true 반환
    }

    /**
     * 사용자의 메인카드 변경하는 메서드입니다.
     *
     * @param id             해당하는 멤버 id
     * @param registerCardId 주거래카드의 id
     */
    public void mainCardUpdate(Long id, Long registerCardId) {
        Members member = findMemberById(id);
        member.updatePaycardId(registerCardId);
        memberRepository.save(member);
    }

    public MemberSummaryResponseDTO getSummaryInfo(Long memberId) {
        Members member = findMemberById(memberId);
        Grade grade = member.getGradeCode();
        MainSummaryResponseDTO pay = payService.getMainPageInfo(memberId);
        CurrentFundingResultResponseDTO funding = fundingFeignClient.getCurrentInfo(memberId);

        return MemberSummaryResponseDTO.builder()
                                       .name(member.getName())
                                       .nickname(member.getNickname())
                                       .gradeCode(grade.getCode())
                                       .gradeName(grade.getName())
                                       .totalDonationCnt(funding.getFundingCount())
                                       .totalDonationAmount(pay.getTotalDonation())
                                       .autoFundingId(funding.getFundingId()) // 수정해야함
                                       .facilityName(funding.getFacilityName())
                                       .facilityImage(funding.getFacilityImage())
                                       .fundingTotalAmount(funding.getFundingTotalAmount())
                                       .build();
    }

    public MainResponseDTO getMainSummary(Long memberId) {
        Long cardId = findMemberById(memberId).getPaycardId();
        MainSummaryResponseDTO mainPageInfo = payService.getMainPageInfo(memberId);
        FundingInfoResponseDTO funding = fundingFeignClient.getAutoFundingInfo(memberId);

        List<CardDTO> myCardList = new ArrayList<>();

        for (CardDTO card : mainPageInfo.getCardList()) {
            if (cardId == card.getRegisteredCardId()) {
                myCardList.add(CardDTO.builder()
                                      .registeredCardId(card.getRegisteredCardId())
                                      .cardId(card.getCardId())
                                      .cardNickname(card.getCardNickname())
                                      .cardIssuer(card.getCardIssuer())
                                      .mainCard(true)
                                      .build());
            }
        }
        int cardCnt = 1;
        for (CardDTO card : mainPageInfo.getCardList()) {
            if (cardId != card.getRegisteredCardId()) {
                myCardList.add(CardDTO.builder()
                                      .registeredCardId(card.getRegisteredCardId())
                                      .cardId(card.getCardId())
                                      .cardNickname(card.getCardNickname())
                                      .cardIssuer(card.getCardIssuer())
                                      .mainCard(false)
                                      .build());
                cardCnt++;
            }
            if (cardCnt == 4) {
                break;
            }
        }

        MainResponseDTO responseDTO = new MainResponseDTO();
        responseDTO.setPaymentAmount(mainPageInfo.getPaymentAmount());
        responseDTO.setTotalDonation(mainPageInfo.getTotalDonation());
        responseDTO.setLinked(true);
        if (funding == null) {
            responseDTO.setLinked(false);
        }
        responseDTO.setCardList(myCardList);
        return responseDTO;

    }

    public List<HistoryResponseDTO> getHistoryList(Long memberId) {
        List<HistoryResponseDTO> responseDTOList = payService.getHistoryList(memberId);
        for (HistoryResponseDTO h : responseDTOList) {
            if (h.getType().equals("출금") || h.getType().equals("입금")) {
                continue;
            }

            for (HistoryDTO detail : h.getDetail()) {
                if (!detail.getType().equals("기부")) {
                    continue;
                }
                Long fundingId = Long.valueOf(detail.getName());
                Optional<FundingResultResponseDTO> funding = fundingFeignClient.getPaymentResult(fundingId);
                if (funding.isPresent()) {
                    String fundingName = funding.get().getFacilityName();
                    detail.setName(fundingName);
                }
            }
        }
        return responseDTOList;
    }
}
