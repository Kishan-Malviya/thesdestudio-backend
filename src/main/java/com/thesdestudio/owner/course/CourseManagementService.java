package com.thesdestudio.owner.course;


import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.exception.ResourceNotFoundException;
import com.thesdestudio.learning.course.Course;
import com.thesdestudio.learning.course.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CourseManagementService {

    private final CourseRepository courseRepository;

    public CourseManagementService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Course createCourse(CreateCourseRequest request) {
        Course course = new Course();
        course.setTitle(request.title().trim());
        course.setSlug(resolveSlug(request.slug(), request.title()));
        course.setDescription(request.description());
        course.setDisplayOrder(
                request.displayOrder() == null ? 0 : request.displayOrder()
        );
        course.setStatus(ContentStatus.DRAFT);

        ensureSlugAvailable(course.getSlug(), null);

        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .sorted(Comparator
                        .comparing(Course::getDisplayOrder, Comparator.nullsFirst(Integer::compareTo))
                        .thenComparing(Course::getTitle))
                .toList();
    }

    @Transactional
    public Course updateCourse(UUID courseId, UpdateCourseRequest request) {
        Course course = getCourse(courseId);
        String slug = normalizeSlug(request.slug());

        ensureSlugAvailable(slug, courseId);

        course.setTitle(request.title().trim());
        course.setSlug(slug);
        course.setDescription(request.description());

        if (request.displayOrder() != null) {
            course.setDisplayOrder(request.displayOrder());
        }

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateStatus(UUID courseId, ContentStatus status) {
        Course course = getCourse(courseId);

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required"
            );
        }

        course.setStatus(status);
        return courseRepository.save(course);
    }

    private Course getCourse(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );
    }

    private String resolveSlug(String requestedSlug, String title) {
        String value = requestedSlug == null || requestedSlug.isBlank()
                ? title
                : requestedSlug;

        return normalizeSlug(value);
    }

    private String normalizeSlug(String value) {
        String slug = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A valid slug could not be generated"
            );
        }

        if (slug.length() > 220) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Slug cannot exceed 220 characters"
            );
        }

        return slug;
    }

    private void ensureSlugAvailable(String slug, UUID currentCourseId) {
        courseRepository.findBySlug(slug).ifPresent(existing -> {
            if (!existing.getId().equals(currentCourseId)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A course with this slug already exists"
                );
            }
        });
    }
}