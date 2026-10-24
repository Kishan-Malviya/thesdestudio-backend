package com.thesdestudio.owner.interview;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.enums.Difficulty;
import com.thesdestudio.interview.InterviewQuestionResponse;
import com.thesdestudio.owner.course.UpdateContentStatusRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/owner/interview-questions")
@PreAuthorize("hasRole('OWNER')")
public class OwnerInterviewQuestionController {

    private final InterviewQuestionManagementService service;

    public OwnerInterviewQuestionController(
            InterviewQuestionManagementService service) {
        this.service = service;
    }

    @PostMapping
    public InterviewQuestionResponse create(
            @Valid @RequestBody CreateInterviewQuestionRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<InterviewQuestionResponse> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) ContentStatus status) {

        return service.getAll(category, difficulty, status);
    }

    @PutMapping("/{questionId}")
    public InterviewQuestionResponse update(
            @PathVariable UUID questionId,
            @Valid @RequestBody UpdateInterviewQuestionRequest request) {
        return service.update(questionId, request);
    }

    @PatchMapping("/{questionId}/status")
    public InterviewQuestionResponse updateStatus(
            @PathVariable UUID questionId,
            @Valid @RequestBody UpdateContentStatusRequest request) {
        return service.updateStatus(questionId, request.status());
    }
}
