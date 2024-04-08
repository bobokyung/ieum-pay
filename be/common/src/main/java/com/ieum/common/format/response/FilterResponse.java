package com.ieum.common.format.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class FilterResponse {

    private void sendJsonResponse(
        HttpServletResponse response, int status, String statusMessage, String message, Optional<String> actionRequired
    ) throws IOException {

        log.info("Sending JSON response - Status: {}, Message: {}", statusMessage, message);

        Gson gson = new Gson();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", statusMessage);
        responseBody.put("message", message);
        actionRequired.ifPresent(action -> responseBody.put("actionRequired", action));
        String jsonString = gson.toJson(responseBody);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
    }

    public void sendTokenReissueResponse(HttpServletResponse response) throws IOException {
        log.warn("Session expired. Advising token refresh.");
        sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED",
                         "토큰이 만료되었습니다. 토큰을 새로 갱신해주십시오.", Optional.of("REFRESH_TOKEN"));
    }

    public void sendLoginRequiredResponse(HttpServletResponse response) throws IOException {
        log.warn("Service requires login.");
        sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "FAILED",
                         "해당 서비스는 로그인이 필요합니다. 로그인 해주십시오.", Optional.empty());
    }

    public void sendTokenInvalidResponse(HttpServletResponse response) throws IOException {
        log.error("Token invalid. Prompting re-login.");
        sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "FAILED",
                         "토큰이 유효하지 않습니다. 다시 로그인 해주십시오.", Optional.empty());
    }
}


