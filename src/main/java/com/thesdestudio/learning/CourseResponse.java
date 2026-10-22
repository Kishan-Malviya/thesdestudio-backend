package com.thesdestudio.learning;

import java.util.UUID;

public record CourseResponse(
        UUID id,
        String title,
        String slug,
        String description,
        Integer displayOrder
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getDisplayOrder()
        );
    }
}
