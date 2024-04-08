package com.ieum.pay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieum.pay.response.CardOcrResponseDTO;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@RestController
@RequestMapping("/api/card")
public class CardOcrController {

    @Value("${naver.cloud.ocr.url}")
    private String ocrUrl;

    @Value("${naver.cloud.client.secret}")
    private String clientSecret;

    @SuppressWarnings({"unchecked"})
    @PostMapping("/ocr")
    public CardOcrResponseDTO extractText(@RequestParam("img") MultipartFile img) throws IOException, ParseException {
        CardOcrResponseDTO nullDTO = CardOcrResponseDTO.builder()
                                                       .cardNumber("")
                                                       .validThru("")
                                                       .build();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        byte[] imageBytes =img.getBytes();

        // byte 배열을 Base64 문자열로 인코딩
        String base64Image = Base64.encodeBase64String(imageBytes);
        String originalFileExtension = null;
        String contentType = img.getContentType();

        if (!ObjectUtils.isEmpty(contentType)) {
            if (contentType.contains("image/jpeg")) {
                originalFileExtension = "jpg";
            } else if (contentType.contains("image/png")) {
                originalFileExtension = "png";
            }
        }

        if (originalFileExtension.isEmpty()) {
            return nullDTO;
        }

        JSONObject requestObject = new JSONObject();

        requestObject.put("requestId", "guide-json-demo");
        requestObject.put("version", "V2");
        requestObject.put("timestamp", Instant.now().toEpochMilli());

        JSONObject data = new JSONObject();
        data.put("format", originalFileExtension);
        data.put("name", "name");
        data.put("data", base64Image);

        JSONArray req_array = new JSONArray();
        req_array.add(data);

        requestObject.put("images", req_array);

        String jsonRequest = objectMapper.writeValueAsString(requestObject);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-OCR-SECRET", clientSecret);

        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ocrUrl, request, String.class);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response.getBody());
        JSONArray images = (JSONArray) jsonObject.get("images");
        JSONObject image = (JSONObject) images.get(0);
        String result = (String) image.get("inferResult");
        if ("SUCCESS".equals(result)) {
            // 인식 성공 처리
            JSONObject creditCard = (JSONObject) image.get("creditCard");
            JSONObject resultValue = (JSONObject) creditCard.get("result");
            String number = (String) ((JSONObject) resultValue.get("number")).get("text");
            String validThru = (String) ((JSONObject) resultValue.get("validThru")).get("text");
            return CardOcrResponseDTO.builder()
                                     .cardNumber(number)
                                     .validThru(validThru)
                                     .build();
        }
        return nullDTO;
    }


}

