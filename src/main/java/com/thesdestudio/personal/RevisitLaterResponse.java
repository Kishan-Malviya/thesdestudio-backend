package com.thesdestudio.personal;

import java.util.UUID;

public record RevisitLaterResponse(
        UUID subtopicId,
        boolean revisitLater
) {
    public static RevisitLaterResponse from(UserSubtopic userSubtopic) {
        return new RevisitLaterResponse(
                userSubtopic.getSubtopic().getId(),
                userSubtopic.isRevisitLater()
        );
    }
}
