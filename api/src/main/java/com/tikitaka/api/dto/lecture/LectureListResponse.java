package com.tikitaka.api.dto.lecture;

import lombok.Data;
import java.util.List;

@Data
public class LectureListResponse {
    private List<LectureDto> allLectures;
    private List<LectureDto> todayLectures;
}
