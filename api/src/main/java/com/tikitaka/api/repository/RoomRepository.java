package com.tikitaka.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tikitaka.api.domain.room.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByIp(String ip);
}

