package com.tikitaka.api.controller;

import com.tikitaka.api.dto.room.RoomStatusResponse;
import com.tikitaka.api.service.room.RoomService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/status")
    public ResponseEntity<RoomStatusResponse> getRoomStatus(@RequestParam Long roomId) {
        RoomStatusResponse response = roomService.getRoomStatus(roomId);
        return ResponseEntity.ok(response);
    }
}
