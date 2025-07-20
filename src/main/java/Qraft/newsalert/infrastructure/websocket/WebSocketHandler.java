package Qraft.newsalert.infrastructure.websocket;

import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.exception.BaseException;
import Qraft.newsalert.exception.Code;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("웹소켓 연결 종료. Session ID: {}", session.getId());
        sessionManager.removeSession(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 1. 클라이언트가 웹소켓 연결 시 URI에서 토큰을 추출하고 토큰이 존재하는지 검사
        String token = extractTokenFromUri(session.getUri());
        if (token == null || token.isBlank()) {
            log.warn("토큰 없이 웹소켓 연결 시도, 연결을 종료합니다.");
            session.close(CloseStatus.BAD_DATA.withReason("Token is required"));
            return;
        }

        // 2. 세션 매니저에 등록
        boolean success = sessionManager.registerSession(token, session);

        if (success) {
            log.info("새로운 웹소켓 연결 성공. Token: {}, Session ID: {}", token, session.getId());
        } else {
            // 3. 동일 토큰으로 연결시도시 연결을 차단
            log.error("중복된 토큰으로 웹소켓 연결 시도, 연결을 차단합니다. Token: {}", token);
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Duplicate connection"));
        }
    }

    private static String extractTokenFromUri(URI uri) {
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .getFirst("token");
    }

    /**
     * 웹소켓을 통해 세션에 연결된 모든 클라이언트에게 업데이트된 뉴스를 브로드캐스트합니다.
     * @param news
     */
    public void broadcastNewsUpdate(News news){
        Map<String, WebSocketSession> sessions = sessionManager.getSessions();

        for(Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession session = entry.getValue();
            if (session.isOpen()) {
                try {
                    String message = convertNewsToJson(news);
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("웹소켓 메시지 전송 실패. Session ID: {}, Error: {}", session.getId(), e.getMessage());
                }
            } else {
                log.warn("웹소켓 세션이 닫혀있음. Session ID: {}", session.getId());
            }
        }

    }

    private String convertNewsToJson(News news) {
        try {
            String newsObject = objectMapper.writeValueAsString(news);
            return newsObject;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
