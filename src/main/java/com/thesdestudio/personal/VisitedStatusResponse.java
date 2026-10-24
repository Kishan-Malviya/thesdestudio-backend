package com.thesdestudio.personal;

import java.time.Instant;
import java.util.UUID;

public record VisitedStatusResponse(
        UUID subtopicId,
        boolean visited,
        Instant lastVisitedAt
) {
    public static VisitedStatusResponse from(UserSubtopic userSubtopic) {
        return new VisitedStatusResponse(
                userSubtopic.getSubtopic().getId(),
                userSubtopic.isVisited(),
                userSubtopic.getLastVisitedAt()
        );
    }
}
