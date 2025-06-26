package com.tikitaka.api.service.lecture;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.lecture.LectureDto;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos.*;
import com.tikitaka.api.repository.CommentRepository;
import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
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

    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ReactRepository reactRepository;
    private final LectureRepository lectureRepository;

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

        if ("many".equals(sort.toLowerCase())) {
            allLectures = allLectures.stream()
                    .sorted((l1, l2) -> Long.compare(
                            questionRepository.countByLecture(l2),
                            questionRepository.countByLecture(l1)
                    ))
                    .collect(Collectors.toList());

            todayLectures = todayLectures.stream()
                    .sorted((l1, l2) -> Long.compare(
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
                    dto.setRoom(lecture.getRoom() != null ? lecture.getRoom().getName() : null);  // 변경된 부분
                    dto.setCreatedAt(lecture.getCreatedAt());

                    if (user.getRole() == UserRole.PROFESSOR) {
                        Long questionCount = questionRepository.countByLecture(lecture);
                        dto.setFrequency(getFrequencyLabel(questionCount));
                    } else {
                        dto.setFrequency(null);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }


    private String getFrequencyLabel(Long questionCount) {
        if (questionCount >= 10) return "많음";
        if (questionCount >= 5) return "보통";
        return "적음";
    }

    @Override
    public List<QuestionDetailDto> getLiveQuestions(Long lectureId, User currentUser) {
        List<Question> questions = questionRepository.findByLecture_LectureIdOrderByCreatedAtDesc(lectureId);

        return questions.stream().map(question -> {
            QuestionDetailDto dto = new QuestionDetailDto();
            dto.setId(question.getQuestionId());
            dto.setContent(question.getContent());
            dto.setStatus(question.getStatus().name());
            dto.setCreatedAt(question.getCreatedAt());

            // 유저 정보
            User user = question.getUser();
            UserDto userDto = new UserDto();
            userDto.setNickname(user.getName());
            userDto.setRole(user.getRole().name());
            userDto.setAvatar(user.getAvatarUrl());
            dto.setUser(userDto);

            // 답변 목록
            List<AnswerDto> answers = commentRepository.findByQuestionOrderByCreatedAtAsc(question)
                    .stream().map(comment -> {
                        AnswerDto answerDto = new AnswerDto();
                        answerDto.setId(comment.getCommentId());
                        answerDto.setContent(comment.getContent());
                        answerDto.setCreatedAt(comment.getCreatedAt());

                        UserSimpleDto responder = new UserSimpleDto();
                        responder.setNickname(comment.getResponder().getName());
                        responder.setRole(comment.getResponder().getRole().name());
                        answerDto.setUser(responder);

                        return answerDto;
                    }).collect(Collectors.toList());
            dto.setAnswer(answers);
            dto.setAnswerCount(Long.valueOf(answers.size()));

            // 리액션 수
            dto.setLikes(reactRepository.countByTargetAndType(question, ReactType.LIKE));
            dto.setWonder(reactRepository.countByTargetAndType(question, ReactType.WONDER));

            // 메달 유무
            boolean hasMedal = reactRepository.countByTargetAndType(question, ReactType.MEDAL) > 0;
            dto.setMedal(hasMedal ? "🥇" : "0");

            boolean likedByCurrentUser = reactRepository
                    .findByUserAndTargetAndType(currentUser, question, ReactType.LIKE)
                    .isPresent();

            boolean wonderedByCurrentUser = reactRepository
                    .findByUserAndTargetAndType(currentUser, question, ReactType.WONDER)
                    .isPresent();

            dto.setLikedByCurrentUser(likedByCurrentUser);
            dto.setWonderedByCurrentUser(wonderedByCurrentUser);

            return dto;
        }).collect(Collectors.toList());
    }

}
