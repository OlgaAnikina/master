package chat.web.rest.controllers;

import chat.model.MyUser;
import chat.services.RoomService;
import chat.web.rest.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

@RestController
public class SpamerController {

    @Autowired
    RoomService roomService;

    public SpamerController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("v2/rooms/{roomsId}/messages/{lastId}")
    public Collection<MessageDTO> getMessageById(
            //@RequestParam(name = "lastId", required = false) Long lastId,
            @PathVariable("lastId") long lastId,
                                                 @PathVariable("roomsId") long roomsId,
                                                 @AuthenticationPrincipal MyUser user) {
        //   LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTime), ZoneId.systemDefault());

        // return roomService.getLastMessages(now, roomsId, user);
        return roomService.getMessagesAfterId(roomsId, user, lastId);
    }


}
