package com.thesdestudio.owner.topic;

import com.thesdestudio.owner.course.UpdateContentStatusRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/owner/courses/{courseId}/topics")
@PreAuthorize("hasRole('OWNER')")
public class OwnerTopicController {
    private final TopicManagementService topicManagementService;

    public OwnerTopicController(TopicManagementService topicManagementService) {
        this.topicManagementService = topicManagementService;
    }

    @PostMapping
    public OwnerTopicResponse createTopic(
            @PathVariable UUID courseId,
            @Valid @RequestBody CreateTopicRequest request
    ) {
        return OwnerTopicResponse.from(
                topicManagementService.createTopic(courseId, request)
        );
    }

    @GetMapping
    public List<OwnerTopicResponse> getAllTopics(
            @PathVariable UUID courseId
    ) {
        return topicManagementService.getAllTopics(courseId)
                .stream()
                .map(OwnerTopicResponse::from)
                .toList();
    }

    @PutMapping("/{topicId}")
    public OwnerTopicResponse updateTopic(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @Valid @RequestBody UpdateTopicRequest request
    ) {
        return OwnerTopicResponse.from(
                topicManagementService.updateTopic(
                        courseId, topicId, request
                )
        );
    }

    @PatchMapping("/{topicId}/status")
    public OwnerTopicResponse updateStatus(
            @PathVariable UUID courseId,
            @PathVariable UUID topicId,
            @Valid @RequestBody UpdateContentStatusRequest request
    ) {
        return OwnerTopicResponse.from(
                topicManagementService.updateStatus(
                        courseId, topicId, request.status()
                )
        );
    }
}
