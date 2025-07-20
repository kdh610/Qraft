package Qraft.newsalert.infrastructure.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * 새로운 세션을 등록합니다.
     * @param token 클라이언트의 고유 토큰
     * @param session 새로 연결된 웹소켓 세션
     * @return 등록 성공 시 true, 이미 동일 토큰의 세션이 있어 실패 시 false
     */
    public boolean registerSession(String token, WebSocketSession session) {
        // putIfAbsent는 해당 키가 없을 때만 값을 넣고 null을 반환하는 원자적(atomic) 연산입니다.
        if (sessions.putIfAbsent(token, session) == null) {
            // 기존에 토큰이 없었으므로 성공적으로 등록됨
            return true;
        }
        // 이미 동일한 토큰으로 연결된 세션이 존재함
        return false;
    }

    /**
     * 세션 연결이 종료될 때, 관리 목록에서 제거합니다.
     * @param session 종료된 웹소켓 세션
     */
    public void removeSession(WebSocketSession session) {
        // Map의 모든 entry를 순회하며, value(WebSocketSession)가 일치하는 항목을 찾아서 제거합니다.
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }

}
