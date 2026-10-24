package com.thesdestudio.practice;

import com.thesdestudio.learning.subtopic.Subtopic;
import com.thesdestudio.learning.subtopic.SubtopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseSlug}/topics/{topicSlug}/subtopics/{subtopicSlug}/practice-questions")
public class PracticeQuestionController {
    private final SubtopicService subtopicService;
    private final PracticeQuestionService practiceQuestionService;

    public PracticeQuestionController(
            SubtopicService subtopicService, PracticeQuestionService practiceQuestionService
    ) {
        this.subtopicService = subtopicService;
        this.practiceQuestionService = practiceQuestionService;
    }

    public ResponseEntity<List<PracticeQuestionResponse>> getPracticeQuestions(
            @PathVariable String courseSlug,
            @PathVariable String topicSlug,
            @PathVariable String subtopicSlug
    ) {
        Subtopic subtopic = subtopicService.getPublishedSubtopic(courseSlug, topicSlug, subtopicSlug);
        List<PracticeQuestionResponse> practiceQuestions = practiceQuestionService.getPublishedQuestions(subtopic.getId())
                .stream()
                .map(PracticeQuestionResponse::from)
                .toList();
        return ResponseEntity.ok(practiceQuestions);
    }


}
