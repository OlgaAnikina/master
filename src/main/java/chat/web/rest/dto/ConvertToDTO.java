package chat.web.rest.dto;

import chat.model.Message;
import chat.model.MyUser;
import chat.model.Room;
import chat.repositories.MessageRepository;
import chat.repositories.RoleRepository;
import chat.repositories.RoomRepository;
import chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public  class ConvertToDTO {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ConvertToDTO(UserRepository userRepository, MessageRepository messageRepository,
                        RoomRepository roomRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.roomRepository= roomRepository;
        this.roleRepository = roleRepository;
    }


    public  MessageDTO convertMessageToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setAuthorName(message.getAuthorName());

        return messageDTO;
    }

    public  Message convertMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setText(messageDTO.getText());
        MyUser author = userRepository.findByUsername(messageDTO.getAuthorName());
        message.setAuthor(author);

        message.setRoom(roomRepository.findById(messageDTO.getRoomId()).get());
        return message;
    }

    public UserDTO convertUserToDTO (MyUser user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getUsername());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    public MyUser convertUser (UserDTO userDTO) {
        MyUser myUser = new MyUser();
        myUser.setId(userDTO.getId());
        myUser.setUsername(userDTO.getName());
        myUser.setRoles(userDTO.getRoles());

        return myUser;
    }

    public RoomDTO convertToRoomDTO(Room room) {

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getRoomsName());


        for(MyUser owner: room.getParticipants() ){
            roomDTO.addParticipant(convertUserToDTO(owner));
        }
        if(room.getMessages() != null) {
            for (Message message : room.getMessages()) {
                roomDTO.addMessage(convertMessageToDTO(message));
            }
        }

        return roomDTO;

    }

    public Room convertToRoom(RoomDTO roomDTO) {
        Room room = new Room();
        room.setRoomsName(roomDTO.getName());

        for(String participanrsName:roomDTO.getParticipantsName()) {
            MyUser user =  userRepository.findByUsername(participanrsName);
            if(user != null)
                room.addParticipants(user);
        }
        for(MessageDTO messageDto: roomDTO.getMessages())
            room.addMessage(convertMessage(messageDto));
        return room;
    }

    public Collection<RoomDTO> convertToDTOListOfRooms (List<Room> rooms) {
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (int i = 0;  i < rooms.size(); i++) {
            roomDTOS.add(convertToRoomDTO((rooms.get(i))));
        }
        return  roomDTOS;
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
