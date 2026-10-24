package com.thesdestudio.personal;

import java.util.UUID;

public record PersonalNotesResponse(
        UUID subtopicId,
        String notes
) {
    public static PersonalNotesResponse from(UserSubtopic userSubtopic) {
        return new PersonalNotesResponse(
                userSubtopic.getSubtopic().getId(),
                userSubtopic.getPersonalNotes()
        );
    }
}
