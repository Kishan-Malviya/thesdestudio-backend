package com.thesdestudio.owner.topic;

import com.thesdestudio.common.enums.ContentStatus;
import com.thesdestudio.common.exception.ResourceNotFoundException;
import com.thesdestudio.learning.course.Course;
import com.thesdestudio.learning.course.CourseRepository;
import com.thesdestudio.learning.topic.Topic;
import com.thesdestudio.learning.topic.TopicRepository;
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
public class TopicManagementService {

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;

    public TopicManagementService(
            CourseRepository courseRepository,
            TopicRepository topicRepository
    ) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public Topic createTopic(UUID courseId, CreateTopicRequest request) {
        Course course = getCourse(courseId);

        Topic topic = new Topic();
        topic.setCourse(course);
        topic.setTitle(request.title().trim());
        topic.setSlug(resolveSlug(request.slug(), request.title()));
        topic.setDescription(request.description());
        topic.setDisplayOrder(
                request.displayOrder() == null ? 0 : request.displayOrder()
        );
        topic.setStatus(ContentStatus.DRAFT);

        ensureSlugAvailable(courseId, topic.getSlug(), null);

        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics(UUID courseId) {
        getCourse(courseId);

        return topicRepository.findByCourse_IdOrderByDisplayOrderAsc(courseId)
                .stream()
                .sorted(Comparator
                        .comparing(Topic::getDisplayOrder,
                                Comparator.nullsFirst(Integer::compareTo))
                        .thenComparing(Topic::getTitle))
                .toList();
    }

    @Transactional
    public Topic updateTopic(
            UUID courseId,
            UUID topicId,
            UpdateTopicRequest request
    ) {
        Topic topic = getTopic(courseId, topicId);
        String slug = normalizeSlug(request.slug());

        ensureSlugAvailable(courseId, slug, topicId);

        topic.setTitle(request.title().trim());
        topic.setSlug(slug);
        topic.setDescription(request.description());

        if (request.displayOrder() != null) {
            topic.setDisplayOrder(request.displayOrder());
        }

        return topicRepository.save(topic);
    }

    @Transactional
    public Topic updateStatus(
            UUID courseId,
            UUID topicId,
            ContentStatus status
    ) {
        Topic topic = getTopic(courseId, topicId);

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required"
            );
        }

        topic.setStatus(status);
        return topicRepository.save(topic);
    }

    private Course getCourse(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );
    }

    private Topic getTopic(UUID courseId, UUID topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found")
                );

        if (!topic.getCourse().getId().equals(courseId)) {
            throw new ResourceNotFoundException(
                    "Topic not found in the specified course"
            );
        }

        return topic;
    }

    private String resolveSlug(String requestedSlug, String title) {
        return normalizeSlug(
                requestedSlug == null || requestedSlug.isBlank()
                        ? title
                        : requestedSlug
        );
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

    private void ensureSlugAvailable(
            UUID courseId,
            String slug,
            UUID currentTopicId
    ) {
        topicRepository.findByCourse_IdAndSlug(courseId, slug)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(currentTopicId)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "A topic with this slug already exists "
                                        + "in the course"
                        );
                    }
                });
    }
}
