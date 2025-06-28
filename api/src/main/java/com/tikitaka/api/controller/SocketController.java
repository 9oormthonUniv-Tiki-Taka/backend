package com.tikitaka.api.controller;

import com.tikitaka.api.dto.socket.LiveSocketRequest;
import com.tikitaka.api.jwt.CustomUserDetails;
import com.tikitaka.api.service.socket.SocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SocketService socketService;

    @MessageMapping("lectures/{lectureId}/live")
    public void handleSocket(@DestinationVariable Long lectureId,
                             @Payload LiveSocketRequest message,
                             @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        CustomUserDetails userDetails = (CustomUserDetails) sessionAttributes.get("user");

        if (userDetails == null) {
            log.warn("🔥 WebSocket 인증되지 않은 요청입니다.");
            return;
        }
        socketService.handleLiveSocket(lectureId, message, userDetails.getUser().getId());

        messagingTemplate.convertAndSend(
                "/topic/lectures/" + lectureId + "/live", message
        );
    }
}
