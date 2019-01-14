package chat.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/messages")
public class MessageController {

    private int counter = 4;

    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{ put("id", "1"); put("text", "First message"); }});
        add(new HashMap<String, String>() {{ put("id", "2"); put("text", "Second message"); }});
        add(new HashMap<String, String>() {{ put("id", "3"); put("text", "Third message"); }});
    }};

    @GetMapping
    public List<Map<String, String>> getAllMessages() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getMessageById(@PathVariable String id) {
        return getMessage(id);
    }

    private Map<String, String> getMessage(@PathVariable String id) {

        Map<String, String> tmp = new HashMap<>();
     for (int i = 0; i < messages.size(); i ++) {
         tmp = messages.get(i);
         if (tmp.containsKey(id)) {
             return tmp;
         }
     }return tmp;
    }

    @PostMapping
    public Map<String, String> createMessage(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }


}
