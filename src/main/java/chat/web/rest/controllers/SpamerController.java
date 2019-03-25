package chat.web.rest.controllers;

import chat.model.MyUser;
import chat.services.RoomService;
import chat.services.UserService;
import chat.web.rest.dto.MessageDTO;
import chat.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class SpamerController {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    public SpamerController(RoomService roomService, UserService userService) {

        this.roomService = roomService;
        this.userService = userService;
    }

    @GetMapping("v2/rooms/{roomsId}/messages/{lastId}")
    public Collection<MessageDTO> getMessageById(
            @PathVariable("lastId") long lastId,
                                                 @PathVariable("roomsId") long roomsId,
                                                 @AuthenticationPrincipal MyUser user) {

        return roomService.getMessagesAfterId(roomsId, user, lastId);
    }

    @GetMapping("v2/users/{idLastUser}")
    public Collection<UserDTO> getUsersRoom(@PathVariable("idLastUser") int idLastUser) {
        if (idLastUser != 0) {

            List<UserDTO> resultUser = new ArrayList<>();
            for (UserDTO userAll : userService.getUsers()) {
                if (userAll.getId() > idLastUser) {
                    resultUser.add(userAll);
                }
            }
            return resultUser;
        }

        return userService.getUsers();
    }




}
