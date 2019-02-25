package chat.services;

import chat.model.Message;
import chat.model.MyUser;
import chat.model.Relation;
import chat.model.Room;
import chat.repositories.MessageRepository;
import chat.repositories.RelationRepository;
import chat.repositories.RoomRepository;
import chat.web.rest.dto.ConvertToDTO;
import chat.web.rest.dto.RoomDTO;
import chat.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ConvertToDTO convertToDTO;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RelationRepository relationRepository;

    public RoomService(RoomRepository roomRepository, ConvertToDTO convertToDTO,
                       MessageRepository messageRepository,
                       RelationRepository relationRepository) {
        this.roomRepository = roomRepository;
        this.convertToDTO = convertToDTO;
        this.messageRepository = messageRepository;
        this.relationRepository = relationRepository;
    }

    public List<RoomDTO> usersRoom(UserDTO user) {

        List<RoomDTO> resultRooms = new ArrayList<>();
        List<Room> roomDB = roomRepository.findAll();
        Collection<RoomDTO> roomsDTO = convertToDTO.convertToDTOListOfRooms(roomDB);

        for (RoomDTO room : roomsDTO) {

            List<String> participants = room.getParticipantsName();
            if ((containUser(room.getParticipantsName(), user.getName()) && !room.getType().equals("PRIVATE"))) {
                resultRooms.add(room);

            }
        }
        return resultRooms;
    }

    private boolean containUser(List<String> roomsProperty, String username) {
        for (int i = 0; i < roomsProperty.size(); i++) {
            if (roomsProperty.get(i).equals(username)) {
                return true;
            }
        }
        return false;
    }

    public List<Message> getCommonMessage() {
        List<Message> commonMessages = new ArrayList<>();
        List<Message> allMessage = messageRepository.findAll();
        for (Message message : allMessage) {
            if (message.getRoomId() == 0) {
                commonMessages.add(message);
            }
        }
        return commonMessages;

    }

    public List<RoomDTO> getRooms(MyUser owner) {
        List<Relation> relations = relationRepository.findAll();
        List<RoomDTO> result = new ArrayList<>();

        for(Relation relation: relations) {
            if((relation.getUser().getUsername().equals(owner.getUsername()))
                    && (relation.getRoom().getType().equals("PUBLIC"))) {
                result.add(convertToDTO.convertToRoomDTO(relation.getRoom()));
            }

        }
        return result;
    }

}
