package com.tikitaka.api.dev.service.question;

import com.tikitaka.api.dev.dto.QuestionRequest;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.question.QuestionStatus;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dev.repository.DevLectureRepository;
import com.tikitaka.api.dev.repository.DevQuestionRepository;
import com.tikitaka.api.dev.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevQuestionServiceImpl implements DevQuestionService {
    private final DevQuestionRepository questionRepository;
    private final DevUserRepository userRepository;
    private final DevLectureRepository lectureRepository;

    @Override
    public void registerQuestion(QuestionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lecture lecture = lectureRepository.findById(request.getLectureId())
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        Question question = Question.builder()
                .user(user)
                .lecture(lecture)
                .content(request.getContent())
                .status(QuestionStatus.WAITING)
                .build();

        questionRepository.save(question);
    }
}
