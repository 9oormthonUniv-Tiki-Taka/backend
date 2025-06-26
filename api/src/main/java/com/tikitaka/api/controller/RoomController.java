package com.tikitaka.api.controller;

import com.tikitaka.api.dto.room.RoomStatusResponse;
import com.tikitaka.api.service.room.RoomService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Room API", description = "강의실 상태 확인 live/idle")
@SecurityRequirement(name = "JWT")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/status")
    public ResponseEntity<RoomStatusResponse> getRoomStatus(@RequestParam Long roomId) {
        RoomStatusResponse response = roomService.getRoomStatus(roomId);
        return ResponseEntity.ok(response);
    }
}
