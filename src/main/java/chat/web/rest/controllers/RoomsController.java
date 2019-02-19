package chat.web.rest.controllers;


import chat.model.MyUser;
import chat.model.Role;
import chat.model.Room;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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


    public RoomsController(ConvertToDTO convertToDTO,
                           RoomRepository roomRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.roomRepository = roomRepository;
        this.convertToDTO = convertToDTO;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        role = roleRepository.findAll();
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
    @Transactional
    public RoomDTO createRoom(Model model, @RequestBody RoomDTO roomDTO,
                             @AuthenticationPrincipal MyUser owner) {


        Room duplicatedRoom = getDuplicatedRoom(roomDTO);
        if(duplicatedRoom != null){
            return convertToDTO.convertToRoomDTO(duplicatedRoom);
        }
        Room room = convertToDTO.convertToRoom(roomDTO);
        owner.addRole(role.get(0));
        room.addParticipants(owner);
        owner.addParticipants(room);
        userRepository.save(owner);

        for (String participantsName : roomDTO.getParticipantsName()) {
            MyUser user = userRepository.findByUsername(participantsName);
            if (user != null) {
                user.addRole(role.get(2));

                user.addParticipants(room);
                userRepository.save(user);
            }

        }

        roomRepository.save(room);
        return convertToDTO.convertToRoomDTO(room);

    }

    private Room getDuplicatedRoom(RoomDTO roomDTO) {
        for(Room room:roomRepository.findAll()){
            List<String> partisipanse  = room.getParticipants()
                    .stream()
                    .map(MyUser::getUsername)
                    .collect(Collectors.toList());
            if(partisipanse.containsAll(roomDTO.getParticipantsName()))
                return room;
        }
        return null;
}


}
