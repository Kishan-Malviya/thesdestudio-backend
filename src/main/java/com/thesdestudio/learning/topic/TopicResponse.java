package com.thesdestudio.learning.topic;

import java.util.UUID;

public record TopicResponse(
        UUID id,
        String title,
        String slug,
        String description,
        Integer displayOrder
) {
    public static TopicResponse from(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getSlug(),
                topic.getDescription(),
                topic.getDisplayOrder()
        );
    }
}
