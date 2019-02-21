package chat.model;

import javax.persistence.*;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String text;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author")
    private MyUser author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "guest";
    }

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public long getRoomId() {
        return this.room.getId();
    }

    @Override
    public String toString() {
        return "Message{ id = " + " text: "
                + text + "  }";
    }
}
