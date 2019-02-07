package chat.web.rest.dto;

import chat.model.Message;
import chat.model.MyUser;
import chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  class ConvertToDTO {
    @Autowired
    private UserRepository userRepository;

    public  MessageDTO convertMessageToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setAuthorName(message.getAuthorName());

        return messageDTO;
    }

    public  Message convertMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setText(messageDTO.getText());
        message.setAuthor(userRepository.findByUsername(messageDTO.getAuthorName()));

        return message;
    }

    public UserDTO convertUserToDTO (MyUser user) {
        UserDTO userDTO = new UserDTO();
       userDTO.setId(user.getId());
        userDTO.setName(user.getUsername());

        return userDTO;
    }

    public MyUser convertUser (UserDTO userDTO) {
        MyUser myUser = new MyUser();
        myUser.setId(userDTO.getId());
        myUser.setUsername(userDTO.getName());

        return myUser;
    }

    public Collection<MessageDTO> convertToDTOListOfMessages (List<Message> messages) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        for (int i = 0;  i < messages.size(); i++) {
            messageDTOS.add(convertMessageToDTO(messages.get(i)));
        }
        return  messageDTOS;
    }

    public Collection<UserDTO> convertToDTOListOfUsers(List<MyUser> users) {
        List<UserDTO> usersDTO = new ArrayList<>();
        for (int i = 0;  i < users.size(); i++) {
            usersDTO.add(convertUserToDTO(users.get(i)));
        }
        return  usersDTO;
    }

}
