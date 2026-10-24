package com.thesdestudio.learning.topic;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.exception.ResourceNotFoundException;
import com.thesdestudio.learning.course.Course;
import com.thesdestudio.learning.course.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TopicService {
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;

    public TopicService(CourseRepository courseRepository, TopicRepository topicRepository) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
    }

    public List<Topic> getPublishedTopicsByCourseSlug(String courseSlug) {
        Course course = getPublishedCourse(courseSlug);
        return topicRepository.findByCourse_IdAndStatusOrderByDisplayOrderAsc(
                course.getId(),
                ContentStatus.PUBLISHED
        );
    }

    public Topic getPublishedTopicBySlugs(String courseSlug, String topicSlug) {
        Course course = getPublishedCourse(courseSlug);
        return topicRepository.findByCourse_IdAndSlug(course.getId(), topicSlug)
                .filter(topic -> topic.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Topic Not Found"));
    }

    private Course getPublishedCourse(String courseSlug) {
        return courseRepository.findBySlug(courseSlug)
                .filter(course -> course.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));
    }
}
