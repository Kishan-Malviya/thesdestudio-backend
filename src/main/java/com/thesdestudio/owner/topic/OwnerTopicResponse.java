package com.thesdestudio.owner.topic;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.learning.topic.Topic;

import java.util.UUID;

public record OwnerTopicResponse(
        UUID id,
        UUID courseId,
        String title,
        String slug,
        String description,
        Integer displayOrder,
        ContentStatus status
) {
    public static OwnerTopicResponse from(Topic topic) {
        return new OwnerTopicResponse(
                topic.getId(),
                topic.getCourse().getId(),
                topic.getTitle(),
                topic.getSlug(),
                topic.getDescription(),
                topic.getDisplayOrder(),
                topic.getStatus()
        );
    }
}
