package com.thesdestudio.owner.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCourseRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200)
        String title,

        @NotBlank(message = "Slug is required")
        @Size(max = 220)
        String slug,

        @Size(max = 10000)
        String description,

        Integer displayOrder
) {
}
