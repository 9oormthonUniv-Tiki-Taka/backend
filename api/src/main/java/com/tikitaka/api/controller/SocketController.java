package com.tikitaka.api.controller;

import com.tikitaka.api.dto.socket.LiveSocketRequest;
import com.tikitaka.api.service.socket.SocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/lectures/{lectureId}/live")
public class SocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SocketService socketService;

    @MessageMapping("") // 클라이언트 전송: /api/lectures/{id}/live
    public void handleSocket(@DestinationVariable Long lectureId,
                             @Payload LiveSocketRequest message,
                             @Header("userId") Long userId) {

        socketService.handleLiveSocket(lectureId, message, userId);

        messagingTemplate.convertAndSend(
                "/api/lectures/" + lectureId + "/live", message
        );
    }
}
