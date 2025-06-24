package com.tikitaka.api.dto.question;

import lombok.Data;
import java.util.List;

@Data
public class QuestionBatchRequest {
    private List<String> questionIDs;
    private String content;
}

