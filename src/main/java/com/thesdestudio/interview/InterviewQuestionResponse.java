package com.thesdestudio.interview;

import com.thesdestudio.common.enums.Difficulty;

import java.util.UUID;

public record InterviewQuestionResponse(
        UUID id,
        String category,
        String questionMarkdown,
        String answerMarkdown,
        Difficulty difficulty,
        Integer displayOrder
) {
    public static InterviewQuestionResponse from(InterviewQuestion interviewQuestion) {
        return new InterviewQuestionResponse(
                interviewQuestion.getId(),
                interviewQuestion.getCategory(),
                interviewQuestion.getQuestionMarkdown(),
                interviewQuestion.getAnswerMarkdown(),
                interviewQuestion.getDifficulty(),
                interviewQuestion.getDisplayOrder()
        );
    }
}
