package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;


    /**
     * 호스트 이름, 포트, 비밀번호를 사용하여 RedisConnectionFactory 를 생성.
     *
     * @return Redis 단독 설정으로 구성된 LettuceConnectionFactory 인스턴스를 반환.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }

    /**
     * 일반 목적 캐싱을 위한 기본 캐시 매니저 빈 구성. NULL 값 캐싱 비활성화, 기본 TTL 설정, 키 직렬화 등의 캐시 동작을 구성.
     *
     * @return 일반 캐싱 요구사항을 위해 구성된 RedisCacheManager 인스턴스를 반환합니다.
     */
    @Primary
    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager() {

        RedisCacheManagerBuilder builder = RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                                                                       .disableCachingNullValues()
                                                                       .entryTtl(Duration.ofDays(1))
                                                                       .computePrefixWith(CacheKeyPrefix.simple())
                                                                       .serializeKeysWith(
                                                                           SerializationPair.fromSerializer(
                                                                               new StringRedisSerializer()));

        return builder.cacheDefaults(configuration)
                      .withInitialCacheConfigurations(getCacheConfigurationMap(configuration))
                      .build();
    }


    /**
     * JSON 기반 캐싱을 위한 캐시 매니저 빈을 구성. 기본 캐시 관리자를 구조화된 데이터 저장을 위한 JSON 값 직렬화로 확장.
     *
     * @return JSON 값 캐싱을 위해 구성된 RedisCacheManager 인스턴스를 반환.
     */
    @Bean(name = "jsonCacheManager")
    public RedisCacheManager jsonCacheManager() {

        RedisCacheManagerBuilder builder = RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                                                                       .disableCachingNullValues()
                                                                       .entryTtl(Duration.ofDays(1))
                                                                       .computePrefixWith(CacheKeyPrefix.simple())
                                                                       .serializeKeysWith(
                                                                           SerializationPair.fromSerializer(
                                                                               new StringRedisSerializer()))
                                                                       .serializeValuesWith(SerializationPair.fromSerializer(
                                                                           new GenericJackson2JsonRedisSerializer(objectMapper)));

        return builder.cacheDefaults(configuration)
                      .withInitialCacheConfigurations(getCacheConfigurationMap(configuration))
                      .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 키 직렬화 방식 설정
        template.setKeySerializer(new StringRedisSerializer());
        // 값 직렬화 방식 설정
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        // 해시 키 직렬화 방식 설정
        template.setHashKeySerializer(new StringRedisSerializer());
        // 해시 값 직렬화 방식 설정
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        // Redis 연결 팩토리 설정
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    private static Map<String, RedisCacheConfiguration> getCacheConfigurationMap(RedisCacheConfiguration configuration) {
        return Map.of(
            "30sec", configuration.entryTtl(Duration.ofSeconds(30)),
            "1min", configuration.entryTtl(Duration.ofMinutes(1)),
            "5min", configuration.entryTtl(Duration.ofMinutes(5)),
            "10min", configuration.entryTtl(Duration.ofMinutes(10)),
            "30min", configuration.entryTtl(Duration.ofMinutes(30)),
            "1hour", configuration.entryTtl(Duration.ofHours(1))
        );
    }
}
