package com.tikitaka.api.dev.controller;

import com.tikitaka.api.dev.dto.RoomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.tikitaka.api.dev.dto.RoomRequest;
import com.tikitaka.api.dev.service.room.DevRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Tag(name = "Dev API", description = "개발용 API 모음")
@RestController
@RequestMapping("/api/dev/rooms")
@RequiredArgsConstructor
public class DevRoomController {
    private final DevRoomService roomService;

    @Operation(summary = "강의실 등록", description = "개발용 강의 등록 API입니다.")
    @PostMapping
    public ResponseEntity<Void> registerRoom(@RequestBody RoomRequest request) {
        roomService.registerRoom(request);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "전체 강의실 조회", description = "모든 강의실 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
}
