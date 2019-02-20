package chat.web.rest.controllers;

import chat.model.MyUser;
import chat.repositories.MessageRepository;
import chat.repositories.RoomRepository;
import chat.repositories.UserRepository;
import chat.services.RoomService;
import chat.services.UserService;
import chat.web.rest.dto.ConvertToDTO;
import chat.web.rest.dto.RoomDTO;
import chat.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
public class RegistrationController {
    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConvertToDTO convertToDTO;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    public RegistrationController(MessageRepository messageRepository,
                                  UserRepository userRepository,
                                  RoomRepository roomRepository,
                                  ConvertToDTO convertToDTO,
                                  RoomService roomService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.convertToDTO = convertToDTO;
        this.roomService = roomService;
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal MyUser user) {
        HashMap<Object, Object> data = new HashMap<>();
        if (user != null) {
            UserDTO profile = convertToDTO.convertUserToDTO(user);
            data.put("profile", profile);
            List<RoomDTO> rooms = roomService.usersRoom(profile);
            data.put("usersRooms",rooms );
            data.put("users",
                    convertToDTO.convertToDTOListOfUsers(userService.getFilterUsers(user)));
        } else {
            data.put("profile", user);
        }

        data.put("messages",
                convertToDTO.convertToDTOListOfMessages(roomService.getCommonMessage()));

        data.put("rooms",
                convertToDTO.convertToDTOListOfRooms(roomRepository.findAll()));

        data.put("currentRoomId",0);

        model.addAttribute("frontendData", data);
        return "index";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(MyUser user) {

        if (!userService.addUser(user)) {
            return "registration";
        }

        return "redirect:/login";
    }


}
