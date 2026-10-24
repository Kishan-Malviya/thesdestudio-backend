package com.thesdestudio.interview;

import com.thesdestudio.common.enums.Difficulty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interview-questions")
public class InterviewQuestionController {
    private final InterviewQuestionService interviewQuestionService;

    public InterviewQuestionController(InterviewQuestionService interviewQuestionService) {
        this.interviewQuestionService = interviewQuestionService;
    }

    public ResponseEntity<List<InterviewQuestionResponse>> getPublishedInterviewQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Difficulty difficulty
    ) {
        List<InterviewQuestionResponse> interviewQuestions = interviewQuestionService
                .getPublishedInterviewQuestions(category, difficulty)
                .stream()
                .map(InterviewQuestionResponse::from)
                .toList();
        return ResponseEntity.ok(interviewQuestions);
    }
}
