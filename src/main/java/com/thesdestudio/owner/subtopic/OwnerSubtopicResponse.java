package com.thesdestudio.owner.subtopic;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.learning.subtopic.Subtopic;

import java.util.UUID;

public record OwnerSubtopicResponse(
        UUID id,
        UUID courseId,
        UUID topicId,
        String title,
        String slug,
        String contentMarkdown,
        String youtubeUrl,
        Integer displayOrder,
        ContentStatus status
) {
    public static OwnerSubtopicResponse from(Subtopic subtopic) {
        return new OwnerSubtopicResponse(
                subtopic.getId(),
                subtopic.getTopic().getCourse().getId(),
                subtopic.getTopic().getId(),
                subtopic.getTitle(),
                subtopic.getSlug(),
                subtopic.getContentMarkdown(),
                subtopic.getYoutubeUrl(),
                subtopic.getDisplayOrder(),
                subtopic.getStatus()
        );
    }
}
