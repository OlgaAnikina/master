package chat.web.rest.controllers;


import chat.model.Message;
import chat.model.MyUser;
import chat.repositories.MessageRepository;
import chat.web.rest.dto.ConvertToDTO;
import chat.web.rest.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    ConvertToDTO convertToDTO;

    @Autowired
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository, ConvertToDTO convertToDTO) {
        this.messageRepository = messageRepository;
        this.convertToDTO = convertToDTO;
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
    public MessageDTO createMessage(@RequestBody MessageDTO message,
                                @AuthenticationPrincipal MyUser user) {
        message.setAuthorName(user.getUsername());
        Message messageDb = convertToDTO.convertMessage(message);
        messageDb.setAuthor(user);
        messageRepository.save(messageDb);
        return message;

    }





}
