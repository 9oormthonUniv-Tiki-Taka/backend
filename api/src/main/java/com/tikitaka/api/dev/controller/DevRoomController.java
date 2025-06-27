package com.tikitaka.api.dev.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tikitaka.api.dev.dto.RoomRequest;
import com.tikitaka.api.dev.service.room.DevRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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

    @Operation(summary = "강의실 강의 추가", description = "개발용 강의실 강의 추가 API입니다.")
    @PostMapping("/{roomId}/lectures/{lectureId}")
    public ResponseEntity<Void> updateRoom(@PathVariable Long roomId, @PathVariable Long lectureId) {
        roomService.updateRoom(roomId, lectureId);
        return ResponseEntity.ok().build();
    }
}
