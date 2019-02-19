package chat.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String roomsName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Message> messages;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
    private List<MyUser> participants = new ArrayList<>();

    public Room() {    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public String getRoomsName() {
        return roomsName;
    }

    public void setRoomsName(String roomsName) {
        this.roomsName = roomsName;
    }

    public List<MyUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MyUser> participants) {
        this.participants = participants;
    }

    public void addParticipants(MyUser participant) {
        participants.add(participant);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
