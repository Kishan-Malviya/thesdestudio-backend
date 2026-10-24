package com.thesdestudio.learning.course;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getPublishedCourses() {
        return courseRepository.findByStatusOrderByDisplayOrderAsc(ContentStatus.PUBLISHED);
    }

    public Optional<Course> getPublishedCourseBySlug(String slug) {
        return courseRepository.findBySlug(slug)
                .filter(course -> course.getStatus() == ContentStatus.PUBLISHED);
    }
}
