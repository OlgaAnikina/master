package chat.web.rest.controllers;

import chat.model.MyUser;
import chat.model.Relation;
import chat.model.Role;
import chat.model.Room;
import chat.repositories.RelationRepository;
import chat.repositories.RoleRepository;
import chat.repositories.RoomRepository;
import chat.repositories.UserRepository;
import chat.web.rest.dto.ConvertToDTO;
import chat.web.rest.dto.RoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("v2/room")
public class RoomsController {

    List<Role> role;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConvertToDTO convertToDTO;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RelationRepository relationRepository;


    public RoomsController(ConvertToDTO convertToDTO,
                           RoomRepository roomRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           RelationRepository relationRepository) {
        this.roomRepository = roomRepository;
        this.convertToDTO = convertToDTO;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.relationRepository = relationRepository;

    }

    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomDTO roomDTO,
                              @AuthenticationPrincipal MyUser owner) {
        role = roleRepository.findAll();
        Room duplicatedRoom = getDuplicatedRoom(roomDTO, owner);
        if (duplicatedRoom != null) {
            return convertToDTO.convertToRoomDTO(duplicatedRoom);
        }
        Room room = convertToDTO.convertToRoom(roomDTO);
        roomRepository.save(room);

        Relation relation = new Relation();
        relation.setRoom(room);
        relation.setUser(owner);
        relation.setRole(role.get(0));
        relationRepository.save(relation);
        room.addRelation(relation);

        for (String participantsName : roomDTO.getParticipantsName()) {
            Relation relationParticipant = new Relation();
            relationParticipant.setRole(role.get(2));
            MyUser user = userRepository.findByUsername(participantsName);
            relationParticipant.setUser(user);
            relationParticipant.setRoom(room);
            relationRepository.save(relationParticipant);
            room.addRelation(relationParticipant);
        }

        roomRepository.save(room);
        return convertToDTO.convertToRoomDTO(room);

    }

    private Room getDuplicatedRoom(RoomDTO roomDTO, MyUser owner) {
        List<Room> rooms = roomRepository.findAll();
        if (rooms != null) {
            for (Room room : rooms) {
                if (roomDTO.getType().equals("PRIVATE")) {
                    String ownerName = owner.getUsername();
                    String participant = roomDTO.getParticipantsName().stream().findAny().orElse(null);
                    if (room.getRoomsName().contains(ownerName)
                            && room.getRoomsName().contains(participant))
                        return room;
                }
                if (roomDTO.getName().equals(room.getRoomsName())) {
                    return room;
                }
            }
        }
        return null;
    }

    @PutMapping("/{roomsId}")
    public void addParticipants(@RequestBody Collection<String> newParticipants,
                                @PathVariable("roomsId") long roomsId) {
        for (String user : newParticipants) {
            Relation relation = new Relation();
            relation.setRoom(roomRepository.findById(roomsId));
            relation.setUser(userRepository.findByUsername(user));
            relation.setRole(roleRepository.findByRolesName("PARTICIPANT"));
            relationRepository.save(relation);
            Room room = roomRepository.findById(roomsId);
            room.addRelation(relation);
            roomRepository.save(room);
        }

    }


}
