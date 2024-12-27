package com.alura.ForoHubAPI.dto.topic;

import java.time.LocalDateTime;

import com.alura.ForoHubAPI.domain.model.Topic;
import com.alura.ForoHubAPI.domain.model.TopicStatus;

public record ListTopicDTO(
    String title,
    LocalDateTime createdAt,
    TopicStatus status,
    Long userId,
    Long courseId
    ) {

    public ListTopicDTO(Topic topic){
        this(topic.getTitle(), topic.getCreatedAt(), topic.getStatus(), 
        topic.getUser().getUserId(), topic.getCourse().getCourseId());
    }    
    
}
