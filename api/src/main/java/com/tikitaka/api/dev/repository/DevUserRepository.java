package com.tikitaka.api.dev.repository;

import com.tikitaka.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevUserRepository extends JpaRepository<User, Long> {
}

