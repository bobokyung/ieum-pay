package com.ieum.alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.ieum.alert.dto.FcmMessage;
import com.ieum.alert.dto.FcmMessage.Message;
import com.ieum.alert.dto.FcmMessage.Notification;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final ObjectMapper objectMapper;

    /**
     * Firebase 에서 액세스 토큰을 가져옵니다.
     *
     * @return 액세스 토큰 문자열
     * @throws IOException 액세스 토큰 가져오기 실패 시 예외 발생
     */
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
            .fromStream(new ClassPathResource("ieum-pay-firebase-adminsdk-3eu9j-0fd1a97c8d.json").getInputStream())
            .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }


    /**
     * 지정된 토큰으로 FCM 푸시 알림을 전송합니다.
     *
     * @param targetToken 대상 디바이스 토큰
     * @param title       알림 제목
     * @param body        알림 본문 내용
     * @throws IOException 네트워크 통신 실패 시 예외 발생
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        String API_URL = "https://fcm.googleapis.com/v1/projects/ieum-pay/messages:send";
        Request request = new Request.Builder()
            .url(API_URL)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build();
        Response response = client.newCall(request).execute();
    }


    private String makeMessage(String token, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                                          .message(Message.builder()
                                                          .token(token)
                                                          .notification(Notification.builder()
                                                                                    .title(title)
                                                                                    .body(body)
                                                                                    .image(null)
                                                                                    .build())
                                                          .build())
                                          .validateOnly(false)
                                          .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

}