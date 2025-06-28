package com.tikitaka.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    // 강의별 세션 저장: Map<lectureId, Set<Session>>
    private final Map<Long, Set<WebSocketSession>> lectureSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uri = session.getUri().toString(); // 예: /ws/lecture/4
        Long lectureId = extractLectureId(uri);

        if (lectureId == null) {
            log.warn("❌ 유효하지 않은 WebSocket URI: {}", uri);
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        session.getAttributes().put("lectureId", lectureId);
        lectureSessions.computeIfAbsent(lectureId, id -> ConcurrentHashMap.newKeySet()).add(session);

        log.info("✅ 연결됨 - session: {}, lectureId: {}", session.getId(), lectureId);
        session.sendMessage(new TextMessage("✅ WebSocket 연결 완료 (lectureId=" + lectureId + ")"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long lectureId = (Long) session.getAttributes().get("lectureId");
        if (lectureId == null) return;

        String payload = message.getPayload();
        log.info("📨 메시지 수신 - lectureId: {}, session: {}, payload: {}", lectureId, session.getId(), payload);

        for (WebSocketSession s : lectureSessions.getOrDefault(lectureId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long lectureId = (Long) session.getAttributes().get("lectureId");

        if (lectureId != null && lectureSessions.containsKey(lectureId)) {
            lectureSessions.get(lectureId).remove(session);
            log.info("👋 연결 종료 - session: {}, lectureId: {}", session.getId(), lectureId);
        }
    }

    // 🔍 /ws/lecture/{lectureId} 에서 lectureId 추출
    private Long extractLectureId(String uri) {
        try {
            Pattern pattern = Pattern.compile(".*/lecture/(\\d+)$");
            Matcher matcher = pattern.matcher(uri);
            if (matcher.matches()) {
                return Long.parseLong(matcher.group(1));
            }
        } catch (Exception e) {
            log.warn("Lecture ID 추출 실패: {}", uri);
        }
        return null;
    }
}
