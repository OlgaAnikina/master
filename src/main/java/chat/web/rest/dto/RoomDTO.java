package chat.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class RoomDTO {

    private long id;
    private String name;
    private List<String> participantsName;
    private List<MessageDTO> messages;
    private String ownerName;
    private String type;

    public RoomDTO() {

        participantsName = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipantsName() {
        return participantsName;
    }

    public void setParticipantsName(List<String> participantsName) {
        this.participantsName = participantsName;
    }

    public void addParticipant(UserDTO user) {
        this.participantsName.add(user.getName());
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessageId(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public void addMessage(MessageDTO message) {
        this.messages.add(message);
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
