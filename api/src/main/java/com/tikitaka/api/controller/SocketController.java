package com.tikitaka.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
@RequestMapping("/api/lectures/{lectureId}/live")
public class SocketController extends TextWebSocketHandler {
}
