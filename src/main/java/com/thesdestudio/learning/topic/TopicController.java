package com.thesdestudio.learning.topic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseSlug}/topics")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<TopicResponse>> getPublishedTopics(@PathVariable String courseSlug) {
        List<TopicResponse> topics = topicService.getPublishedTopicsByCourseSlug(courseSlug)
                .stream()
                .map(TopicResponse::from)
                .toList();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{topicSlug}")
    public ResponseEntity<TopicResponse> getPublishedTopic(@PathVariable String courseSlug, @PathVariable String topicSlug) {
        TopicResponse topic = TopicResponse.from(topicService.getPublishedTopicBySlugs(courseSlug, topicSlug));
        return ResponseEntity.ok(topic);
    }
}
