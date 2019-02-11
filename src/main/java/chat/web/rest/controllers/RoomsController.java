package chat.web.rest.controllers;


import chat.model.MyUser;
import chat.model.Role;
import chat.model.Room;
import chat.repositories.RoomRepository;
import chat.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v2/rooms")
public class RoomsController {

    @Autowired
    private RoomRepository roomRepository;


    @PostMapping
    public String createRoom(@RequestBody UserDTO participant,
                              @AuthenticationPrincipal MyUser owner) {
        Room room = new Room();
       // owner.addRole(Role.OWNER);
        //participant.addRole(Role.PARTICIPANT);
        room.addOwner(owner);
        //room.addParticipants(participant);
        roomRepository.save(room);
        return "room";

    }


}
