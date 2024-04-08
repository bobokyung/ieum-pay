package com.ieum.common.service;

import com.ieum.common.domain.Members;
import com.ieum.common.dto.token.TokenInfo;
import com.ieum.common.exception.member.MemberNotFoundException;
import com.ieum.common.exception.token.RefreshTokenNotFoundException;
import com.ieum.common.exception.token.TokenOperationException;
import com.ieum.common.jwt.TokenProvider;
import com.ieum.common.repository.MemberRepository;
import com.ieum.common.util.CookieUtil;
import com.ieum.common.util.RedisHashUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RedisHashUtil redisHashUtil;
    private final CookieUtil cookieUtil;

    private final String MEMBER_ID = "member-id";
    private final String ACCESS_TOKEN = "access-token";
    private final String PHONE_NUMBER = "phone-number";
    private final String REFRESH_TOKEN_KEY = "refresh-token:";


    /**
     * 주어진 TokenInfo 객체를 사용하여 리프레시 토큰과 관련 정보를 Redis 에 저장.
     *
     * @param tokenInfo 저장할 토큰 정보를 담고 있는 TokenInfo 객체
     */
    public Long save(TokenInfo tokenInfo) {
        String key = REFRESH_TOKEN_KEY + tokenInfo.getRefreshToken();
        redisHashUtil.save(key, Map.of(
            MEMBER_ID, tokenInfo.getMemberId(),
            PHONE_NUMBER, tokenInfo.getPhoneNumber(),
            ACCESS_TOKEN, tokenInfo.getAccessToken()
        ), tokenProvider.getREFRESH_TOKEN_TIME());

        return tokenInfo.getMemberId();
    }

    /**
     * 주어진 리프레시 토큰 값에 해당하는 정보를 Redis 에서 삭제합니다. 만약 삭제에 실패하면, TokenOperationException 예외를 던집니다.
     *
     * @param refreshTokenValue 삭제할 리프레시 토큰 값
     * @throws TokenOperationException 토큰 삭제 과정에서 문제가 발생한 경우
     */
    public void remove(String refreshTokenValue) {
        try {
            redisHashUtil.delete(REFRESH_TOKEN_KEY + refreshTokenValue);
        } catch (Exception e) {
            throw new TokenOperationException();
        }
    }


    @Transactional(readOnly = true)
    public String reIssueAccessToken(HttpServletRequest servletRequest) {

        String key = REFRESH_TOKEN_KEY + cookieUtil.getRefreshTokenValue(servletRequest);

        // 리프레시 토큰에 해당하는 정보를 Redis 에서 조회
        Map<String, Object> refreshTokenHash = redisHashUtil.findByKey(key);
        if (refreshTokenHash.isEmpty()) {
            throw new RefreshTokenNotFoundException();
        }

        long memberId = Long.parseLong(refreshTokenHash.get(MEMBER_ID).toString());
        Members members = memberRepository.findById((memberId))
                                          .orElseThrow(MemberNotFoundException::new);

        // 새 액세스 토큰 생성
        String updatedAccessToken = tokenProvider.createAccessToken(members);
        // 새 액세스 토큰 정보 업데이트
        redisHashUtil.updateField(key, ACCESS_TOKEN, updatedAccessToken);

        return updatedAccessToken;
    }

    public String extractAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }


}
