package com.ieum.mms.service;

import com.sun.mail.imap.IMAPFolder;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IMAPService {

    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";

    private final StringRedisTemplate stringRedisTemplate;
    private ExecutorService executor;

    private Store store;
    private Folder emailFolder;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @PostConstruct
    public void startListening() {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            init();
        });
    }

    @PreDestroy
    public void stopListening() {
        try {
            log.info("action:shutdown_attempt, message:Attempting to shut down executor...");
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("action:shutdown_timeout, message:Executor did not terminate in the allotted time.");
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
            log.error("action:interrupted, message:Shutdown interrupted, executor forced to shutdown.");
        }
        closeResources();
    }


    /**
     * IMAP 서버에 연결하여 INBOX 폴더를 열고 새 메시지를 감시합니다. 새 메시지가 도착하면 특정 로직(예: 전화번호 인증 코드 검증)을 실행합니다.
     */
    private void init() {
        try {
            Session emailSession = Session.getDefaultInstance(initProps());
            store = emailSession.getStore("imaps");
            store.connect(host, username, password);

            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            emailFolder.addMessageCountListener(new MessageCountAdapter() {
                public void messagesAdded(MessageCountEvent ev) {
                    Message[] messages = ev.getMessages();
                    log.info("Got " + messages.length + " new messages");
                    for (Message message : messages) {
                        try {
                            Address[] fromAddresses = message.getFrom();
                            String phoneNumber = extractPhoneNumber(fromAddresses).substring(0, 11);

                            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
                            String code = valueOps.get("phone-number:" + phoneNumber);
                            log.info(phoneNumber);
                            if (code == null) {
                                log.info("Redis 에 인증 코드가 없음", phoneNumber);
                                continue;
                            }

                            String content = extractContent(message);
                            String extractedCode = extractCodeFromContent(content, "code:");

                            if (extractedCode != null && extractedCode.equals(extractCodeFromContent(code, "code:"))) {
                                valueOps.set("confirmed-phone-number:" + phoneNumber, "true", 10, TimeUnit.MINUTES);
                                stringRedisTemplate.delete("phone-number:" + phoneNumber);
                                log.info("인증 성공: " + phoneNumber);
                            } else {
                                log.info("인증 실패: " + phoneNumber);
                            }
                        } catch (MessagingException | IOException e) {
                            log.error("메일 처리 중 오류 발생", e);
                        }
                    }
                }
            });

            // IDLE 커맨드로 폴더 감시 유지
            while (!Thread.interrupted()) {
                ((IMAPFolder) emailFolder).idle();
            }
        } catch (MessagingException e) {
            log.error("IMAP 연결 실패", e);
        } finally {
            closeResources();
        }
    }

    /**
     * 이메일 메시지 발신자 주소에서 전화번호를 추출합니다.
     *
     * @param fromAddresses 이메일 발신자 주소 배열
     * @return 추출된 전화번호 문자열, 추출할 수 없는 경우 "Unknown" 반환
     */
    private String extractPhoneNumber(Address[] fromAddresses) {
        if (fromAddresses == null || fromAddresses.length == 0) {
            return "Unknown";
        }
        return Arrays.stream(fromAddresses)
                     .findFirst()
                     .map(Address::toString)
                     .orElse("Unknown")
                     .replaceAll(".*<(\\d+)@.*", "$1");
    }

    /**
     * 이메일 메시지에서 콘텐츠를 추출합니다.
     *
     * @param message 이메일 메시지 객체
     * @return 메시지의 텍스트 또는 HTML 콘텐츠를 담은 문자열
     * @throws MessagingException 메시지 콘텐츠 접근 중 문제 발생 시
     * @throws IOException        I/O 오류 발생 시
     */
    private String extractContent(Message message) throws MessagingException, IOException {
        String contentType = message.getContentType();
        StringBuilder content = new StringBuilder();

        if (contentType.contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            for (int partCount = 0; partCount < multiPart.getCount(); partCount++) {
                BodyPart part = multiPart.getBodyPart(partCount);
                if (part.isMimeType(TEXT_PLAIN) || part.isMimeType(TEXT_HTML)) {
                    content.append(part.getContent().toString());
                }
            }
        } else if (contentType.contains(TEXT_PLAIN) || contentType.contains(TEXT_HTML)) {
            content.append(message.getContent().toString());
        }
        return content.toString();
    }

    /**
     * 이메일 콘텐츠에서 코드를 추출합니다.
     *
     * @param content    이메일 메시지 콘텐츠 문자열
     * @param codePrefix 코드를 식별하는 접두사
     * @return 추출된 코드 문자열, 찾을 수 없는 경우 null 반환
     */
    private String extractCodeFromContent(String content, String codePrefix) {
        int codeStartIdx = content.indexOf(codePrefix);
        if (codeStartIdx != -1) {
            String afterCode = content.substring(codeStartIdx + codePrefix.length());
            int endIdx = afterCode.indexOf('%');
            if (endIdx != -1) {
                // 여기서 endIdx + 1 대신 endIdx를 사용하여 '%' 이후의 내용을 제외합니다.
                return afterCode.substring(0, endIdx).trim();
            }
        }
        return null;
    }


    /**
     * IMAP 세션을 위한 속성을 초기화합니다.
     *
     * @return IMAP 세션 속성이 설정된 Properties 객체
     */
    private Properties initProps() {
        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        return properties;
    }

    /**
     * 이메일 폴더와 스토어 연결을 안전하게 종료합니다.
     */
    private void closeResources() {
        try {
            if (emailFolder != null && emailFolder.isOpen()) {
                emailFolder.close(false);
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            log.error("리소스 정리 실패", e);
        }
    }
}
