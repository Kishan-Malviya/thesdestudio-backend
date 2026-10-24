package com.thesdestudio.learning.subtopic;

import java.util.UUID;

public record SubtopicResponse(
        UUID id,
        String title,
        String slug,
        String contentMarkdown,
        String youtubeUrl,
        Integer displayOrder
) {
    public static SubtopicResponse from (Subtopic subtopic) {
        return new SubtopicResponse(
                subtopic.getId(),
                subtopic.getTitle(),
                subtopic.getSlug(),
                subtopic.getContentMarkdown(),
                subtopic.getYoutubeUrl(),
                subtopic.getDisplayOrder()
        );
    }
}
