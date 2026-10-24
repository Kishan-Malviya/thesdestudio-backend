package com.thesdestudio.learning.course;

import com.thesdestudio.common.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findBySlug(String slug);

    List<Course> findByStatusOrderByDisplayOrderAsc(ContentStatus status);
}
