package chat.web.rest.controllers;


import chat.model.Message;
import chat.model.MyUser;
import chat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("v1/messages")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("v1/messages/{id}")
    public Message getMessageById(@PathVariable("id") Message message) {
        return message;
    }


    @PostMapping("v1/messages")
    public Message createMessage(@RequestBody Message message,
                                @AuthenticationPrincipal MyUser user) {
        message.setAuthor(user);
        return messageRepository.save(message);
    }


}
