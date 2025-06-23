package com.tikitaka.api.dto.lecture;

import lombok.Data;
import java.util.List;

@Data
public class LectureListResponse {
    private List<LectureDto> lectures;
}
