package com.tikitaka.api.service;

import com.tikitaka.api.domain.comment.Comment;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.question.QuestionStatus;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.question.*;
import com.tikitaka.api.repository.CommentRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ReactRepository reactRepository;
    private final UserRepository userRepository;
    private final GptService gptService;



    // --- 질문 목록 조회 (페이징, 필터, 정렬 포함) ---
    @Override
    public Page<QuestionListDto> getQuestions(Long lectureId, UserRole role, String statusStr, String sort, int page) {
        QuestionStatus status = null;
        if (statusStr != null) {
            if (statusStr.equalsIgnoreCase("waiting")) {
                status = QuestionStatus.WAITING;
            } else if (statusStr.equalsIgnoreCase("answered")) {
                status = QuestionStatus.ANSWERED;
            }
        }

        Pageable pageable;
        if ("old".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, 5, org.springframework.data.domain.Sort.by("createdAt").ascending());
        } else { // 기본 recent (최신순)
            pageable = PageRequest.of(page, 5, org.springframework.data.domain.Sort.by("createdAt").descending());
        }

        Page<Question> questionPage = questionRepository.findByLectureIdAndStatus(lectureId, status, pageable);

        List<QuestionListDto> dtoList = questionPage.stream().map(q -> {
            QuestionListDto dto = new QuestionListDto();
            dto.setId(q.getQuestionId());
            dto.setContent(q.getContent());
            dto.setCreatedAt(q.getCreatedAt());

            if (role == UserRole.PROFESSOR) {
                dto.setStatus(q.getStatus().name().toLowerCase());
            } else {
                dto.setStatus(null);
            }

            long medalCount = reactRepository.countByTargetAndType(q, ReactType.MEDAL);
            dto.setMedal(medalCount > 0 ? "gold" : null);

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, questionPage.getTotalElements());
    }

    // --- 질문 상세 조회 (학생 전용) ---
    @Override
    public QuestionDtos.QuestionDetailResponse getQuestionDetail(Long lectureId, Long questionId, Long userId) {
        Question question = questionRepository.findByLectureIdAndQuestionIdWithUser(lectureId, questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        List<Comment> comments = commentRepository.findAllByQuestionOrderByCreatedAtAsc(question);

        Long likes = reactRepository.countByTargetAndType(question, ReactType.LIKE);
        Long wonder = reactRepository.countByTargetAndType(question, ReactType.WONDER);
        Long medalCount = reactRepository.countByTargetAndType(question, ReactType.MEDAL);
        String medal = medalCount > 0 ? "gold" : null;

        String questionUserNickname = "티키 " + (question.getUser().getId() % 100 + 1);

        QuestionDtos.QuestionDetailDto dto = new QuestionDtos.QuestionDetailDto();
        dto.setId(question.getQuestionId());
        dto.setContent(question.getContent());
        dto.setStatus(question.getStatus().name().toLowerCase());

        QuestionDtos.UserDto userDto = new QuestionDtos.UserDto();
        userDto.setNickname(questionUserNickname);
        userDto.setRole(question.getUser().getRole().name());
        userDto.setAvatar(question.getUser().getAvatarUrl());
        dto.setUser(userDto);

        List<QuestionDtos.AnswerDto> answerDtoList = comments.stream().map(c -> {
            QuestionDtos.AnswerDto answerDto = new QuestionDtos.AnswerDto();
            answerDto.setId(c.getCommentId());

            QuestionDtos.UserSimpleDto responderDto = new QuestionDtos.UserSimpleDto();
            responderDto.setNickname("티키 " + (c.getResponder().getId() % 100 + 1));
            responderDto.setRole(c.getResponder().getRole().name());
            answerDto.setUser(responderDto);

            answerDto.setContent(c.getContent());
            answerDto.setCreatedAt(c.getCreatedAt());

            return answerDto;
        }).collect(Collectors.toList());

        dto.setAnswer(answerDtoList);
        dto.setAnswerCount(Long.valueOf(answerDtoList.size()));
        dto.setCreatedAt(question.getCreatedAt());
        dto.setMedal(medal);
        dto.setLikes(likes);
        dto.setWonder(wonder);

        QuestionDtos.QuestionDetailResponse response = new QuestionDtos.QuestionDetailResponse();
        response.setQuestion(dto);

        return response;
    }
    // 댓글/답변 등록
    @Override
    public void postComment(Long lectureId, Long questionId, Long userId, String content) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        Comment comment = Comment.builder()
                .question(question)
                .responder(user)
                .content(content)
                .build();

        commentRepository.save(comment);
    }
    public void deleteComment(Long lectureId, Long questionId, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // 댓글이 해당 질문에 속하는지 체크
        if (!comment.getQuestion().getQuestionId().equals(questionId)) {
            throw new IllegalArgumentException("Comment does not belong to the question");
        }

        // 댓글 작성자(user)와 요청자(userId) 비교
        if (!comment.getResponder().getId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this comment");
        }

        // 작성자가 학생인지 확인
        if (comment.getResponder().getRole() != UserRole.STUDENT) {
            throw new SecurityException("Only students can delete their comments");
        }

        commentRepository.delete(comment);
    }
    @Override
    public AiResponseDto getAIAnswer(Long lectureId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        if (!question.getLecture().getLectureId().equals(lectureId)) {
            throw new IllegalArgumentException("Question does not belong to lecture");
        }

        String prompt = question.getContent();
        String aiAnswerContent = gptService.getGptAnswer(prompt);

        AiResponseDto response = new AiResponseDto();
        response.setContent(aiAnswerContent);
        return response;
    }
    @Override
    public QuestionBatchResponse answerQuestionsBatch(Long lectureId, QuestionBatchRequest request) {
        List<Long> questionIds = request.getQuestionIDs().stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // 강의에 해당하는 질문 조회
        List<Question> questions = questionRepository.findAllById(questionIds).stream()
                .filter(q -> q.getLecture().getLectureId().equals(lectureId))
                .collect(Collectors.toList());

        if (questions.size() != questionIds.size()) {
            throw new EntityNotFoundException("Some questions not found or do not belong to the lecture");
        }

        // 질문 상태를 ANSWERED로 변경
        questions.forEach(q -> q.updateStatus(QuestionStatus.ANSWERED));

        // 저장
        questionRepository.saveAll(questions);

        List<QuestionBatchResponse.QuestionBatchDto> batchDtos = questions.stream().map(q -> {
            QuestionBatchResponse.QuestionBatchDto dto = new QuestionBatchResponse.QuestionBatchDto();
            dto.setId(String.valueOf(q.getQuestionId()));
            dto.setStatus(q.getStatus().name().toLowerCase());
            dto.setContent(q.getContent());
            dto.setCreatedAt(q.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        QuestionBatchResponse response = new QuestionBatchResponse();
        response.setQuestions(batchDtos);
        return response;
    }

}
