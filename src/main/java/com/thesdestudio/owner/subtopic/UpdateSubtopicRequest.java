package com.thesdestudio.owner.subtopic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateSubtopicRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200)
        String title,

        @NotBlank(message = "Slug is required")
        @Size(max = 220)
        String slug,

        @NotNull(message = "Content must not be null")
        @Size(max = 100000)
        String contentMarkdown,

        @Size(max = 2048)
        String youtubeUrl,

        Integer displayOrder
) {
}
