package com.tikitaka.api.service;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.lecture.LectureDto;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos;
import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final QuestionRepository questionRepository;

    @Override
    public LectureListResponse getLectureList(User user, String sort) {
        Sort sortOption = switch (sort.toLowerCase()) {
            case "name" -> Sort.by("name").ascending();
            case "many" -> Sort.by("createdAt").descending();
            case "recent" -> Sort.by("createdAt").descending();
            default -> Sort.by("createdAt").descending();
        };

        List<Lecture> allLectures = lectureRepository.findByUser(user, sortOption);

        DayOfWeek today = DayOfWeek.from(LocalDate.now());

        List<Lecture> todayLectures = allLectures.stream()
                .filter(lecture -> lecture.getDayOfWeek() == today)
                .collect(Collectors.toList());

        // 질문 수 정렬 필요하면 별도 처리
        if ("many".equals(sort.toLowerCase())) {
            allLectures = allLectures.stream()
                    .sorted((l1, l2) -> Integer.compare(
                            questionRepository.countByLecture(l2),
                            questionRepository.countByLecture(l1)
                    ))
                    .collect(Collectors.toList());

            todayLectures = todayLectures.stream()
                    .sorted((l1, l2) -> Integer.compare(
                            questionRepository.countByLecture(l2),
                            questionRepository.countByLecture(l1)
                    ))
                    .collect(Collectors.toList());
        }

        List<LectureDto> allDtoList = mapToDtoList(allLectures, user);
        List<LectureDto> todayDtoList = mapToDtoList(todayLectures, user);

        LectureListResponse response = new LectureListResponse();
        response.setAllLectures(allDtoList);
        response.setTodayLectures(todayDtoList);

        return response;
    }

    private List<LectureDto> mapToDtoList(List<Lecture> lectures, User user) {
        return lectures.stream()
                .map(lecture -> {
                    LectureDto dto = new LectureDto();
                    dto.setId(lecture.getLectureId());
                    dto.setName(lecture.getName());
                    dto.setRoom(lecture.getRoom());
                    dto.setCreatedAt(lecture.getCreatedAt());

                    if (user.getRole() == UserRole.PROFESSOR) {
                        int questionCount = questionRepository.countByLecture(lecture);
                        dto.setFrequency(getFrequencyLabel(questionCount));
                    } else {
                        dto.setFrequency(null);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private String getFrequencyLabel(int questionCount) {
        if (questionCount >= 10) return "많음";
        if (questionCount >= 5) return "보통";
        return "적음";
    }

    @Override
    public List<QuestionDtos> getLiveQuestions(Long lectureId) {
        // 구현 내용 작성
        return null; // 임시 반환
    }
}
