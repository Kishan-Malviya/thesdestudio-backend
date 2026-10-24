package com.thesdestudio.owner.practice;

import com.thesdestudio.owner.course.UpdateContentStatusRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        "/api/v1/owner/courses/{courseId}/topics/{topicId}"
                + "/subtopics/{subtopicId}/practice-questions")
@PreAuthorize("hasRole('OWNER')")
public class OwnerPracticeQuestionController {

    private final PracticeQuestionManagementService service;

    public OwnerPracticeQuestionController(
            PracticeQuestionManagementService service) {
        this.service = service;
    }

    @PostMapping
    public OwnerPracticeQuestionResponse create(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @PathVariable UUID subtopicId,
            @Valid @RequestBody CreatePracticeQuestionRequest request) {

        return service.create(courseId, topicId, subtopicId, request);
    }

    @GetMapping
    public List<OwnerPracticeQuestionResponse> getAll(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @PathVariable UUID subtopicId) {

        return service.getAll(courseId, topicId, subtopicId);
    }

    @PutMapping("/{questionId}")
    public OwnerPracticeQuestionResponse update(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @PathVariable UUID subtopicId,
            @PathVariable UUID questionId,
            @Valid @RequestBody UpdatePracticeQuestionRequest request) {

        return service.update(
                courseId, topicId, subtopicId, questionId, request);
    }

    @PatchMapping("/{questionId}/status")
    public OwnerPracticeQuestionResponse updateStatus(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @PathVariable UUID subtopicId,
            @PathVariable UUID questionId,
            @Valid @RequestBody UpdateContentStatusRequest request) {

        return service.updateStatus(
                courseId, topicId, subtopicId,
                questionId, request.status());
    }
}
