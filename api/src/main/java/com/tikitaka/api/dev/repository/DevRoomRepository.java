package com.tikitaka.api.dev.repository;

import com.tikitaka.api.domain.room.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevRoomRepository extends JpaRepository<Room, Long> {
}
