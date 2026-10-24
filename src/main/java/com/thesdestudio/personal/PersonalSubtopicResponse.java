package com.thesdestudio.personal;

import java.time.Instant;
import java.util.UUID;

public record PersonalSubtopicResponse(
        UUID subtopicId,
        String subtopicTitle,
        String subtopicSlug,
        String topicTitle,
        String topicSlug,
        String courseTitle,
        String courseSlug,
        boolean visited,
        boolean revisitLater,
        Instant lastVisitedAt
) {
    public static PersonalSubtopicResponse from(UserSubtopic userSubtopic) {
        var subtopic = userSubtopic.getSubtopic();
        var topic = subtopic.getTopic();
        var course = topic.getCourse();

        return new PersonalSubtopicResponse(
                subtopic.getId(),
                subtopic.getTitle(),
                subtopic.getSlug(),
                topic.getTitle(),
                topic.getSlug(),
                course.getTitle(),
                course.getSlug(),
                userSubtopic.isVisited(),
                userSubtopic.isRevisitLater(),
                userSubtopic.getLastVisitedAt()
        );
    }
}
