package chat.web.rest.controllers;

import chat.model.MyUser;
import chat.model.Relation;
import chat.repositories.*;
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
    private RoleRepository roleRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    public RegistrationController(MessageRepository messageRepository,
                                  UserRepository userRepository,
                                  RoomRepository roomRepository,
                                  ConvertToDTO convertToDTO,
                                  RoomService roomService,
                                  RoleRepository roleRepository,
                                  RelationRepository relationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.convertToDTO = convertToDTO;
        this.roomService = roomService;
        this.roleRepository = roleRepository;
        this.relationRepository = relationRepository;
    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal MyUser user) {
        HashMap<Object, Object> data = new HashMap<>();
        if (user != null) {
            UserDTO profile = convertToDTO.convertUserToDTO(user);
            data.put("profile", profile);

            List<RoomDTO> rooms = roomService.getRooms(user);
            data.put("usersRooms", rooms);
            data.put("users",
                    convertToDTO.convertToDTOListOfUsers(userService.getFilterUsers(user)));
        } else {
            data.put("profile", user);
        }

        data.put("messages",
                convertToDTO.convertToDTOListOfMessages(roomService.getCommonMessage()));

        data.put("rooms",
                convertToDTO.convertToDTOListOfRooms(roomRepository.findAll()));

        data.put("currentRoomId", 0);
        data.put("currentRoom", convertToDTO.convertToRoomDTO(roomRepository.findById(0)));

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

        Relation relation = new Relation();
        relation.setRoom(roomRepository.findById(0));
        relation.setUser(user);
        relation.setRole(roleRepository.findByRolesName("PARTICIPANT"));
        relationRepository.save(relation);
        return "redirect:/login";
    }


}
