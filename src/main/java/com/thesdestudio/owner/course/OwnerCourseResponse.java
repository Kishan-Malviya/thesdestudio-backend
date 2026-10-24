package com.thesdestudio.owner.course;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.learning.course.Course;

import java.util.UUID;

public record OwnerCourseResponse(
        UUID id,
        String title,
        String slug,
        String description,
        Integer displayOrder,
        ContentStatus status
) {
    public static OwnerCourseResponse from(Course course) {
        return new OwnerCourseResponse(
                course.getId(),
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getDisplayOrder(),
                course.getStatus()
        );
    }
}
