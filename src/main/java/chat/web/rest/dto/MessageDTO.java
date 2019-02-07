package chat.web.rest.dto;

public class MessageDTO {

    private long id;
    private String text;
  //  private UserDTO author;
    private String authorName;

    public MessageDTO() {
    }

  //  public MessageDTO(long id, String text, UserDTO author) {
    public MessageDTO(long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.authorName = author;
     //   this.author = author;
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

  /*  public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }*/

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
