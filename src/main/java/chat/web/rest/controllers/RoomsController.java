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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v2/room")
public class RoomsController {

    List<Role> role;
    List<Relation> rel;

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

    @GetMapping
    public String getRoomsIncludedUsers(Model model, @AuthenticationPrincipal MyUser owner) {
        HashMap<Object, Object> data = new HashMap<>();
        List<RoomDTO> resultRooms = new ArrayList<>();
        List<Room> roomList = roomRepository.findAll();
        System.out.println(roomList);
        Collection<RoomDTO> roomsDTO = convertToDTO.convertToDTOListOfRooms(roomList);
        for (RoomDTO room : roomsDTO) {
            if ((room.getParticipantsName().contains(owner.getUsername()))) {
                resultRooms.add(room);
            }
        }
        data.put("usersRooms", resultRooms);
        model.addAttribute("frontendData", data);
        return "index";
    }


    @PostMapping
    public RoomDTO createRoom(Model model, @RequestBody RoomDTO roomDTO,
                             @AuthenticationPrincipal MyUser owner) {

        role = roleRepository.findAll();
        Room duplicatedRoom = getDuplicatedRoom(roomDTO);
        if(duplicatedRoom != null){
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

        for (String participantsName:roomDTO.getParticipantsName()){
            Relation relationParticipant = new Relation();
            relationParticipant.setRole(role.get(2));
            MyUser user = userRepository.findByUsername(participantsName);
            relationParticipant.setUser(user);
            relationParticipant.setRoom(room);
            relationRepository.save(relationParticipant);
            room.addRelation(relationParticipant);
        }

        return convertToDTO.convertToRoomDTO(room);

    }

    private String buildRoomName(RoomDTO room){
        StringBuilder builder = new StringBuilder();
        List<String> relations = new ArrayList<>();
        Collections.copy(room.getParticipantsName(), relations);
        relations.add(room.getOwnerName());
        builder.append(relations);
        return builder.toString();
    }

    private Room getDuplicatedRoom(RoomDTO roomDTO) {
        List<Room> rooms = roomRepository.findAll();
        if(rooms != null){
            for(Room room: rooms){
                if(room.getRoomsName().equals(roomDTO.getName()))
                    return room;
            }
        }
        return null;
}


}
