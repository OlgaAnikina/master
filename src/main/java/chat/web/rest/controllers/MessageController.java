package chat.web.rest.controllers;


import chat.model.Message;
import chat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/messages")
public class MessageController {

    @Autowired
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    public Message getMessageById(@PathVariable("id") Message message) {
        return message;
    }


    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }


}
