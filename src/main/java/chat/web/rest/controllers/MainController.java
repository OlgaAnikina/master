package chat.web.rest.controllers;

import chat.model.Message;
import chat.model.MyUser;
import chat.model.Role;
import chat.repositories.MessageRepository;
import chat.repositories.UserRepository;
import chat.web.rest.dto.ConvertToDTO;
import chat.web.rest.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final UserRepository userRepository;

    ConvertToDTO convertToDTO = new ConvertToDTO();

    @Autowired
    public MainController(MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal MyUser user) {
        HashMap<Object, Object> data = new HashMap<>();


        data.put("profile", user);
        data.put("messages",
                convertToDTO.convertToDTOListOfMessages(messageRepository.findAll()));
      /*  data.put("users",
                convertToDTO.convertToDTOListOfUsers(userRepository.findAll()));*/
        model.addAttribute("frontendData", data);
        return "index";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(MyUser user) {

        MyUser userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        userRepository.save(user);

        return "redirect:/login";

    }


}
