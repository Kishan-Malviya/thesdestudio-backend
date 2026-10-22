package com.thesdestudio.learning;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getPublishedCourses() {
        List<CourseResponse> courses = courseService.getPublishedCourses()
                .stream()
                .map(CourseResponse::from)
                .toList();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CourseResponse> getPublishedCourseBySlug(@PathVariable String slug) {
        return ResponseEntity.of(
                courseService.getPublishedCourseBySlug(slug)
                        .map(CourseResponse::from)
        );
    }
}
