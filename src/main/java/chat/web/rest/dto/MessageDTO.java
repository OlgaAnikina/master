package chat.web.rest.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class MessageDTO {

    private long id;
    private String text;
    private Long roomId;
    private String authorName;
    private long createdWhen;

    public MessageDTO() {
    }

    public MessageDTO(long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.authorName = author;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public long getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(long createdWhen) {
        this.createdWhen = createdWhen;
    }
}
