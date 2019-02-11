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

    @ManyToMany
    @JoinTable(name = "roomsowner",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<MyUser> owners = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "rooms_participants",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<MyUser> participants = new ArrayList<>();




    public Room() {
    }


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

    public List<MyUser> getOwners() {
        return owners;
    }

    public void setOwners(List<MyUser> owners) {
        this.owners = owners;
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

    public void addOwner(MyUser owner) {
        owners.add(owner);
    }

    public void addParticipants(MyUser participant) {
        participants.add(participant);
    }
}
