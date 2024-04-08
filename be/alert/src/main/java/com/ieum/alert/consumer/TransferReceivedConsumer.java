package com.ieum.alert.consumer;

import com.google.gson.Gson;
import com.ieum.alert.document.FcmToken;
import com.ieum.alert.message.TransferReceivedMessage;
import com.ieum.alert.repository.FcmTokenRepository;
import com.ieum.alert.service.FcmService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferReceivedConsumer {

    private static final String NOTIFICATION_TITLE = "송금";

    private final Gson gson;
    private final FcmService fcmService;
    private final FcmTokenRepository fcmTokenRepository;

    @KafkaListener(topics = "${topic.transferReceived}",
                   groupId = "${consumer.transferReceived}")
    public void handleTransferReceivedMessage(@Payload String messageJson) {
        TransferReceivedMessage message = gson.fromJson(messageJson, TransferReceivedMessage.class);
        fcmTokenRepository.findFcmTokenByMemberId(message.getReceiverId())
                          .ifPresentOrElse(
                              fcmToken -> sendFcmNotification(fcmToken, message),
                              () -> log.info("FCM 토큰이 없어 알림을 전송하지 않았습니다. receiverId={}", message.getReceiverId())
                          );
    }

    private void sendFcmNotification(FcmToken fcmToken, TransferReceivedMessage message) {
        String notificationBody = String.format("%s님이 %d원을 송금하셨습니다.", message.getSenderName(), message.getAmount());
        try {
            fcmService.sendMessageTo(fcmToken.getToken(), NOTIFICATION_TITLE, notificationBody);
            log.info("FCM 알림 전송 성공: receiverId={}, notificationBody={}", message.getReceiverId(), notificationBody);
        } catch (IOException e) {
            log.error("FCM 알림 전송 실패: receiverId={}, notificationBody={}", message.getReceiverId(), notificationBody, e);
        }
    }
}
