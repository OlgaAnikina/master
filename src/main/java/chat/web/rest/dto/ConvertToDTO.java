package chat.web.rest.dto;

import chat.model.Message;
import chat.model.MyUser;
import chat.model.Relation;
import chat.model.Room;
import chat.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConvertToDTO {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RelationRepository relationRepository;

    public ConvertToDTO(UserRepository userRepository, MessageRepository messageRepository,
                        RoomRepository roomRepository, RoleRepository roleRepository,
                        RelationRepository relationRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.relationRepository = relationRepository;
    }


    public MessageDTO convertMessageToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setAuthorName(message.getAuthorName());

        return messageDTO;
    }

    public Message convertMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setText(messageDTO.getText());
        MyUser author = userRepository.findByUsername(messageDTO.getAuthorName());
        message.setAuthor(author);

        message.setRoom(roomRepository.findById(messageDTO.getRoomId()).get());
        return message;
    }

    public UserDTO convertUserToDTO(MyUser user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getUsername());

        return userDTO;
    }

    public MyUser convertUser(UserDTO userDTO) {
        MyUser myUser = new MyUser();
        myUser.setId(userDTO.getId());
        myUser.setUsername(userDTO.getName());
        return myUser;
    }

    public RoomDTO convertToRoomDTO(Room room) {

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getRoomsName());
        roomDTO.setType(room.getType());

        for (Relation relation : room.getRoomRelations()) {
            if (relation.getRole().getRolesName().equals("PARTICIPANT")) {
                roomDTO.addParticipant(convertUserToDTO(relation.getUser()));
            } else if (relation.getRole().getRolesName().equals("OWNER")) {
                roomDTO.setOwnerName(relation.getUser().getUsername());
            }
        }

        if (room.getMessages() != null) {
            for (Message message : room.getMessages()) {
                roomDTO.addMessage(convertMessageToDTO(message));
            }
        }

        return roomDTO;

    }

    public Room convertToRoom(RoomDTO roomDTO) {
        Room room = new Room();
        room.setRoomsName(roomDTO.getName());
        room.setType(roomDTO.getType());
        for (MessageDTO messageDto : roomDTO.getMessages())
            room.addMessage(convertMessage(messageDto));
        return room;
    }

    public Collection<RoomDTO> convertToDTOListOfRooms(List<Room> rooms) {
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            roomDTOS.add(convertToRoomDTO((rooms.get(i))));
        }
        return roomDTOS;
    }

    public Collection<MessageDTO> convertToDTOListOfMessages(List<Message> messages) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            messageDTOS.add(convertMessageToDTO(messages.get(i)));
        }
        return messageDTOS;
    }

    public Collection<UserDTO> convertToDTOListOfUsers(List<MyUser> users) {
        List<UserDTO> usersDTO = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            usersDTO.add(convertUserToDTO(users.get(i)));
        }
        return usersDTO;
    }

    public RelationDTO convertRelarionToDTO(Relation relation) {
        RelationDTO relationDTO = new RelationDTO();
        relationDTO.setId(relation.getId());
        relationDTO.setRoomName(relation.getRoom().getRoomsName());
        relationDTO.setUserName(relation.getUser().getUsername());
        relationDTO.setRole(relation.getRole().getRolesName());
        return relationDTO;
    }

    public Relation DtoRelationconvert(RelationDTO relationDTO) {
        Relation relation = new Relation();
        relation.setId(relationDTO.getId());

        relation.setRoom(roomRepository.findByRoomsName(relationDTO.getRoomName()));
        relation.setRole(roleRepository.findByRolesName(relationDTO.getRole()));
        relation.setUser(userRepository.findByUsername(relationDTO.getUserName()));

        return relation;
    }


}
