package com.thesdestudio.owner.course;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/owner/courses")
@PreAuthorize("hasRole('OWNER')")
public class OwnerCourseController {
    private final CourseManagementService courseManagementService;

    public OwnerCourseController(CourseManagementService courseManagementService) {
        this.courseManagementService = courseManagementService;
    }

    @PostMapping
    public OwnerCourseResponse createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return OwnerCourseResponse.from(
                courseManagementService.createCourse(request)
        );
    }

    @GetMapping
    public List<OwnerCourseResponse> getAllCourses() {
        return courseManagementService.getAllCourses()
                .stream()
                .map(OwnerCourseResponse::from)
                .toList();
    }

    @PutMapping("/{courseId}")
    public OwnerCourseResponse updateCourse(
            @PathVariable UUID courseId,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        return OwnerCourseResponse.from(
                courseManagementService.updateCourse(courseId, request)
        );
    }

    @PatchMapping("/{courseId}/status")
    public OwnerCourseResponse updateStatus(
            @PathVariable UUID courseId,
            @Valid @RequestBody UpdateContentStatusRequest request
    ) {
        return OwnerCourseResponse.from(
                courseManagementService.updateStatus(
                        courseId,
                        request.status()
                )
        );
    }
}
