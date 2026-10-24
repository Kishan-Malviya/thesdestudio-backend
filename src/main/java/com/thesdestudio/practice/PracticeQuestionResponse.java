package com.thesdestudio.practice;

import com.thesdestudio.common.enums.Difficulty;

import java.util.UUID;

public record PracticeQuestionResponse(
        UUID id,
        String questionMarkdown,
        String answerMarkdown,
        Difficulty difficulty,
        Integer displayOrder
) {
    public static PracticeQuestionResponse from (PracticeQuestion practiceQuestion) {
        return new PracticeQuestionResponse(
                practiceQuestion.getId(),
                practiceQuestion.getQuestionMarkdown(),
                practiceQuestion.getAnswerMarkdown(),
                practiceQuestion.getDifficulty(),
                practiceQuestion.getDisplayOrder()
        );
    }
}
