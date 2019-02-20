package chat.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String roomsName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Message> messages;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Relation> roomRelations;

    @Column
    private String type;

    public Room() {
        roomRelations = new ArrayList<>();
        messages = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getRoomsName() {
        return roomsName;
    }

    public void setRoomsName(String roomsName) {
        this.roomsName = roomsName;
    }

    /*  public List<MyUser> getParticipants() {
          return participants;
      }

      public void setParticipants(List<MyUser> participants) {
          this.participants = participants;
      }

      public void addParticipants(MyUser participant) {
          participants.add(participant);
      }
  */
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public List<Relation> getRoomRelations() {
        return roomRelations;
    }

    public void setRoomRelations(List<Relation> roomRelations) {
        this.roomRelations = roomRelations;
    }

    public void addRelation(Relation relation) {
        this.roomRelations.add(relation);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

