package com.alura.ForoHubAPI.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alura.ForoHubAPI.domain.model.Reply;
import com.alura.ForoHubAPI.domain.model.Topic;
import com.alura.ForoHubAPI.domain.model.User;
import com.alura.ForoHubAPI.domain.repository.ReplyRepository;
import com.alura.ForoHubAPI.domain.repository.TopicRepository;
import com.alura.ForoHubAPI.domain.repository.UserRepository;
import com.alura.ForoHubAPI.dto.reply.RegisterReplyDTO;
import com.alura.ForoHubAPI.dto.reply.ReplyDTO;
import com.alura.ForoHubAPI.dto.reply.UpdateReplyDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/replies")
@SecurityRequirement(name = "bearer-key")
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;

    @PostMapping
    public ResponseEntity<ReplyDTO> registerReply(@RequestBody @Valid RegisterReplyDTO data,
                                                UriComponentsBuilder uriBuilder){

        User user = userRepository.getReferenceById(data.authorId());
        Topic topic = topicRepository.getReferenceById(data.topicId());

        Reply reply = new Reply(data);
        reply.setUser(user);
        reply.setTopic(topic);

        Reply replySaved = replyRepository.save(reply);

        URI url = uriBuilder.path("/replies/{replyId}").buildAndExpand(replySaved.getReplyId()).toUri();

        return ResponseEntity.created(url).body(new ReplyDTO(replySaved));
    }

    @GetMapping
    public ResponseEntity<Page<ReplyDTO>> getAllReplies(Pageable pageable){
        return ResponseEntity.ok(replyRepository.findAll(pageable).map(ReplyDTO::new));
    }


    @PutMapping
    @Transactional
    public ResponseEntity<ReplyDTO> updateReply(@RequestBody @Valid UpdateReplyDTO data){
        Reply reply = replyRepository.getReferenceById(data.replyId());

        reply.updateData(data);

        return ResponseEntity.ok(new ReplyDTO(replyRepository.save(reply)));
    }
    

    @DeleteMapping("/{replyId}")
    @Transactional
    public ResponseEntity<Void> deleteReply(@PathVariable long replyId){
        replyRepository.deleteById(replyId);
        return ResponseEntity.noContent().build();
    }
    
}
