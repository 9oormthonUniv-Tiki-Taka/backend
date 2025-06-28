package com.tikitaka.api.service.search;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.search.SearchRequest;
import com.tikitaka.api.dto.search.SearchResponse;

import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final LectureRepository lectureRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ReactRepository reactRepository;

    @Override
    public SearchResponse search(Long userId, SearchRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String role = user.getRole().name();

        if ("question".equals(request.getType())) {
            return searchQuestions(role, request.getQuery());
        } else if ("lecture".equals(request.getType())) {
            return searchLectures(role, request.getQuery());
        } else {
            throw new IllegalArgumentException("Invalid search type: " + request.getType());
        }
    }

    private SearchResponse searchQuestions(String role, String query) {
        List<Question> questions = questionRepository.findByContentContainingIgnoreCase(query);

        List<Object> results = questions.stream().map(q -> {
            Long medal = reactRepository.countByTargetAndType(q, ReactType.MEDAL);
            if ("PROFESSOR".equals(role)) {
                return new SearchResponse.QuestionResultForProfessor(
                        q.getQuestionId().toString(),
                        q.getContent(),
                        q.getStatus().name().toLowerCase(),
                        q.getCreatedAt(),
                        medal.toString()
                );
            } else {
                return new SearchResponse.QuestionResultForStudent(
                        q.getQuestionId().toString(),
                        q.getContent(),
                        q.getCreatedAt(),
                        medal.toString()
                );
            }
        }).toList();

        return new SearchResponse("question", results);
    }

    private SearchResponse searchLectures(String role, String query) {
        List<Lecture> lectures = lectureRepository.findByNameContainingIgnoreCase(query);

        List<Object> results = lectures.stream().map(l -> {
            String roomName = l.getRoom() != null ? l.getRoom().getName() : "unknown";

            if ("PROFESSOR".equals(role)) {
                Long frequency = questionRepository.countByLecture(l);
                return new SearchResponse.LectureResultForProfessor(
                        l.getLectureId().toString(),
                        l.getName(),
                        roomName,   // String 타입으로 넣음
                        frequency.toString(),
                        l.getCreatedAt()
                );
            } else {
                return new SearchResponse.LectureResultForStudent(
                        l.getLectureId().toString(),
                        l.getName(),
                        roomName,  // String 타입으로 넣음
                        l.getCreatedAt()
                );
            }
        }).toList();
        return new SearchResponse("lecture", results);
    }
}
