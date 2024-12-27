package com.alura.ForoHubAPI.service.topic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alura.ForoHubAPI.domain.model.Course;
import com.alura.ForoHubAPI.domain.model.Topic;
import com.alura.ForoHubAPI.domain.model.User;
import com.alura.ForoHubAPI.domain.repository.CourseRepository;
import com.alura.ForoHubAPI.domain.repository.TopicRepository;
import com.alura.ForoHubAPI.domain.repository.UserRepository;
import com.alura.ForoHubAPI.dto.topic.NewTopicDTO;
import com.alura.ForoHubAPI.dto.topic.RegisterTopicDTO;
import com.alura.ForoHubAPI.infrastructure.errors.exception.BusinessRulesValidationsException;
import com.alura.ForoHubAPI.service.topic.validations.ValidatorPostingTopic;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private List<ValidatorPostingTopic> postValidators;

    
    public NewTopicDTO publish(RegisterTopicDTO data){

        Optional<User> userFound = userRepository.findById(data.authorId());
        
        Optional<Course> courseFound = courseRepository.findById(data.courseId());

        if (userFound.isEmpty()) {
            throw new BusinessRulesValidationsException("El usuario para crear el topico no existe");
        }

        if (courseFound.isEmpty()) {
            throw new BusinessRulesValidationsException("El Curso al que hace referencia no existe");
        }

        // VALIDATION
        postValidators.forEach(v -> v.validate(data));


        User user = userFound.get();
        Course course = courseFound.get();

        Topic topic = new Topic(data);
        topic.setUser(user);
        topic.setCourse(course);

        return new  NewTopicDTO(topicRepository.save(topic));
    }


}