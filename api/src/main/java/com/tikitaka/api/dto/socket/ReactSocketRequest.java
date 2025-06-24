package com.tikitaka.api.dto.socket;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReactSocketRequest {
    private Long questionId;
    private String type;
    private Integer amount;
}
