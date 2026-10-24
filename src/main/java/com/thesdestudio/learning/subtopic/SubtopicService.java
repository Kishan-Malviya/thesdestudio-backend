package com.thesdestudio.learning.subtopic;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.exception.ResourceNotFoundException;
import com.thesdestudio.learning.course.Course;
import com.thesdestudio.learning.course.CourseRepository;
import com.thesdestudio.learning.topic.Topic;
import com.thesdestudio.learning.topic.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SubtopicService {
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;

    public SubtopicService(
            CourseRepository courseRepository,
            TopicRepository topicRepository,
            SubtopicRepository subtopicRepository
    ) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
    }

    public List<Subtopic> getPublishedSubtopics(String courseSlug, String topicSlug) {
        Course course = getPublishedCourse(courseSlug);
        Topic topic = getPublishedTopic(course.getId(), topicSlug);

        return subtopicRepository.findByTopic_IdAndStatusOrderByDisplayOrderAsc(topic.getId(), ContentStatus.PUBLISHED);
    }

    public Subtopic getPublishedSubtopic(String courseSlug, String topicSlug, String subtopicSlug) {
        Course course = getPublishedCourse(courseSlug);
        Topic topic = getPublishedTopic(course.getId(), topicSlug);

        return subtopicRepository.findByTopic_IdAndSlug(topic.getId(), subtopicSlug)
                .filter(subtopic -> subtopic.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Subtopic not found"));
    }

    private Course getPublishedCourse(String courseSlug) {
        return courseRepository.findBySlug(courseSlug)
                .filter(course -> course.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private Topic getPublishedTopic(UUID courseId, String topicSlug) {
        return topicRepository.findByCourse_IdAndSlug(courseId, topicSlug)
                .filter(topic -> topic.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
    }
}
