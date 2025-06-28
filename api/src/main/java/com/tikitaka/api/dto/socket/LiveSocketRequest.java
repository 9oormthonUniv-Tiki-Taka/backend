package com.tikitaka.api.dto.socket;

import java.util.Map;

import lombok.Data;

@Data
public class LiveSocketRequest {
    private String type;
    private Map<String, Object> request;
}
