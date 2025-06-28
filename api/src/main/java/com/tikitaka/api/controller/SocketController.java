package com.tikitaka.api.controller;

import com.tikitaka.api.dto.socket.LiveSocketRequest;
import com.tikitaka.api.jwt.CustomUserDetails;
import com.tikitaka.api.service.socket.SocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        CustomUserDetails safeUserDetails = (userDetails != null) ? userDetails : CustomUserDetails.temp();
        socketService.handleLiveSocket(lectureId, message, safeUserDetails.getUser().getId());

        messagingTemplate.convertAndSend(
                "/topic/lectures/" + lectureId + "/live", message
        );
    }
}
