package com.tikitaka.api.repository;

import com.tikitaka.api.domain.react.React;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactRepository extends JpaRepository<React, Long>{
    
}
