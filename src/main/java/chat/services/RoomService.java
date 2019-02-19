package chat.services;

import chat.model.Room;
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

    public RoomService(RoomRepository roomRepository, ConvertToDTO convertToDTO) {
        this.roomRepository = roomRepository;
        this.convertToDTO = convertToDTO;
    }

    public List<RoomDTO> usersRoom(UserDTO user) {

        List<RoomDTO> resultRooms = new ArrayList<>();
        List<Room> roomDB = roomRepository.findAll();
        Collection<RoomDTO> roomsDTO = convertToDTO.convertToDTOListOfRooms(roomDB);
        for (RoomDTO room : roomsDTO) {

            List<String> participants = room.getParticipantsName();
            if ((containUser(room.getParticipantsName(), user.getName()))) {
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

}
