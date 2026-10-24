package com.thesdestudio.owner.practice;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.enums.Difficulty;
import com.thesdestudio.practice.PracticeQuestion;

import java.util.UUID;

public record OwnerPracticeQuestionResponse(
        UUID id,
        UUID subtopicId,
        String questionMarkdown,
        String answerMarkdown,
        Difficulty difficulty,
        Integer displayOrder,
        ContentStatus status
) {
    public static OwnerPracticeQuestionResponse from(PracticeQuestion practiceQuestion) {
        return new OwnerPracticeQuestionResponse(
                practiceQuestion.getId(),
                practiceQuestion.getSubtopic().getId(),
                practiceQuestion.getQuestionMarkdown(),
                practiceQuestion.getAnswerMarkdown(),
                practiceQuestion.getDifficulty(),
                practiceQuestion.getDisplayOrder(),
                practiceQuestion.getStatus()
        );
    }
}
