package com.ieum.alert.repository;

import com.ieum.alert.document.FcmToken;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FcmTokenRepository extends MongoRepository<FcmToken, String> {

    Optional<FcmToken> findFcmTokenByMemberId(Long memberId);
}
