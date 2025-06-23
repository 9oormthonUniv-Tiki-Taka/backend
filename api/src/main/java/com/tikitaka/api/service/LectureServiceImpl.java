package com.tikitaka.api.service.impl;

import com.tikitaka.api.domain.comment.Comment;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.lecture.LectureDto;
import com.tikitaka.api.dto.question.QuestionDtos;
import com.tikitaka.api.repository.CommentRepository;
import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ReactRepository reactRepository;

    @Override
    public List<QuestionDtos.QuestionDetailDto> getLiveQuestions(Long lectureId) {
        List<Question> questions = questionRepository.findByLectureId(lectureId);

        return questions.stream().map(question -> {
            Comment comment = commentRepository.findFirstByQuestionOrderByCreatedAtAsc(question).orElse(null);

            int likes = reactRepository.countByTargetAndReactType(question, ReactType.LIKE);
            int wonder = reactRepository.countByTargetAndReactType(question, ReactType.WONDER);
            int medal = reactRepository.countByTargetAndReactType(question, ReactType.MEDAL);

            String nickname = "티키 " + (question.getUser().getUserId() % 100 + 1); // 랜덤 닉네임

            return toDto(question, comment, likes, wonder, medal, nickname);
        }).collect(Collectors.toList());
    }

    private QuestionDtos.QuestionDetailDto toDto(Question q, Comment c, int likes, int wonder, int medal, String nickname) {
        QuestionDtos.QuestionDetailDto dto = new QuestionDtos.QuestionDetailDto();

        dto.setId(String.valueOf(q.getQuestionId()));
        dto.setContent(q.getContent());
        dto.setStatus(q.getStatus().name().toLowerCase());

        // User DTO
        QuestionDtos.UserDto userDto = new QuestionDtos.UserDto();
        userDto.setNickname(nickname);
        userDto.setRole(q.getUser().getRole().name());
        userDto.setAvatar(q.getUser().getAvatarUrl());
        dto.setUser(userDto);

        // Answer DTO (nullable)
        if (c != null) {
            QuestionDtos.AnswerDto answer = new QuestionDtos.AnswerDto();
            answer.setId(String.valueOf(c.getCommentId()));

            QuestionDtos.UserSimpleDto responderDto = new QuestionDtos.UserSimpleDto();
            responderDto.setNickname("티키 " + (c.getResponder().getUserId() % 100 + 1));
            responderDto.setRole(c.getResponder().getRole().name());
            answer.setUser(responderDto);

            answer.setContent(c.getContent());
            answer.setCreatedAt(c.getCreatedAt());
            dto.setAnswer(Collections.singletonList(answer));
        } else {
            dto.setAnswer(Collections.emptyList());
        }

        dto.setCreatedAt(q.getCreatedAt());
        dto.setLikes(likes);
        dto.setWonder(wonder);
        dto.setMedal(medal > 0 ? "gold" : null);

        return dto;
    }

    @Override
    public List<LectureDto> getLectureList(UserRole role, String sort) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        List<Lecture> lectures;

        if ("today".equals(sort)) {
            lectures = lectureRepository.findAllByDayOfWeek(today);
        } else {
            lectures = lectureRepository.findAll();
        }

        // 강의별 질문 개수 조회 및 Map 변환
        List<Long> lectureIds = lectures.stream()
                .map(Lecture::getLectureId)
                .collect(Collectors.toList());

        Map<Long, Long> questionCounts = countQuestionsMap(lectureIds);

        // 정렬 Comparator 설정
        Comparator<Lecture> comparator = switch (sort) {
            case "many" -> Comparator.comparing(l -> questionCounts.getOrDefault(l.getLectureId(), 0L), Comparator.reverseOrder());
            case "name" -> Comparator.comparing(Lecture::getName);
            case "recent" -> Comparator.comparing(Lecture::getCreatedAt).reversed();
            default -> Comparator.comparing(Lecture::getCreatedAt).reversed();
        };

        lectures.sort(comparator);

        // DTO 변환
        return lectures.stream().map(lecture -> {
            LectureDto dto = new LectureDto();
            dto.setId(String.valueOf(lecture.getLectureId()));
            dto.setName(lecture.getName());
            dto.setRoom(lecture.getRoom());
            dto.setCreatedAt(lecture.getCreatedAt());

            if (role == UserRole.PROFESSOR) {
                long count = questionCounts.getOrDefault(lecture.getLectureId(), 0L);
                String freq = count >= 20 ? "많음" : count >= 10 ? "보통" : "적음";
                dto.setFrequency(freq);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    private Map<Long, Long> countQuestionsMap(List<Long> lectureIds) {
        if (lectureIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Object[]> results = questionRepository.countQuestionsByLectureIds(lectureIds);

        return results.stream()
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> (Long) r[1]
                ));
    }
}

