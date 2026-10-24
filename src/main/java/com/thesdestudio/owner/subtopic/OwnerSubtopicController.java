package com.thesdestudio.owner.subtopic;

import com.thesdestudio.owner.course.UpdateContentStatusRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        "/api/v1/owner/courses/{courseId}/topics/{topicId}/subtopics")
@PreAuthorize("hasRole('OWNER')")
public class OwnerSubtopicController {

    private final SubtopicManagementService subtopicManagementService;

    public OwnerSubtopicController(
            SubtopicManagementService subtopicManagementService) {
        this.subtopicManagementService = subtopicManagementService;
    }

    @PostMapping
    public OwnerSubtopicResponse createSubtopic(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @Valid @RequestBody CreateSubtopicRequest request) {

        return subtopicManagementService.createSubtopic(
                courseId, topicId, request);
    }

    @GetMapping
    public List<OwnerSubtopicResponse> getAllSubtopics(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId) {

        return subtopicManagementService.getAllSubtopics(courseId, topicId);
    }

    @PutMapping("/{subtopicId}")
    public OwnerSubtopicResponse updateSubtopic(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @PathVariable UUID subtopicId,
            @Valid @RequestBody UpdateSubtopicRequest request) {

        return subtopicManagementService.updateSubtopic(
                courseId, topicId, subtopicId, request);
    }

    @PatchMapping("/{subtopicId}/status")
    public OwnerSubtopicResponse updateStatus(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @PathVariable UUID subtopicId,
            @Valid @RequestBody UpdateContentStatusRequest request) {

        return subtopicManagementService.updateStatus(
                courseId, topicId, subtopicId, request.status());
    }
}