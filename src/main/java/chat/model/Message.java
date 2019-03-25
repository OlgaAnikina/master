package chat.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
public class Message implements Serializable {

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

    @Column(name = "createdWhen")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdWhen;

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

    public LocalDateTime getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(LocalDateTime createdWhen) {
        this.createdWhen = createdWhen;
    }

    @Override
    public String toString() {
        return "Message{ id = " + " text: "
                + text + "  }";
    }
}
