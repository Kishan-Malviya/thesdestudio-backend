package com.thesdestudio.owner.practice;

import com.thesdestudio.common.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePracticeQuestionRequest(
        @NotBlank(message = "Question is requried")
        @Size(max = 100000)
        String questionMarkdown,

        @Size(max = 100000)
        String answerMarkdown,

        @NotNull
        Difficulty difficulty,

        Integer displayOrder
) {
}
