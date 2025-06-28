package com.tikitaka.api.dto.socket;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnswerSocketRequest {
    private Long questionId;
    private String content;
}
