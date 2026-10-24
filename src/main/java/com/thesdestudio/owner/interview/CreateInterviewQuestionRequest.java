package com.thesdestudio.owner.interview;

import com.thesdestudio.common.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateInterviewQuestionRequest(
        @NotBlank(message = "Category is requried")
        @Size(max = 100)
        String category,

        @NotBlank(message = "Question is requried")
        @Size(max = 100000)
        String questionMarkdown,

        @Size(max = 100000)
        String answerMarkdown,

        @NotNull(message = "Difficulty is requried")
        Difficulty difficulty,

        Integer displayOrder
) {
}
