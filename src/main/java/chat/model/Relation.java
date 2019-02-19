package chat.model;

import javax.persistence.*;

@Entity
@Table(name = "rooms_participants")
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany((mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER))
    private MyUser user;
    private Room room;
    private Role role;

    public void Relation(){}

    public Relation(long user_id, long room_id, long role_id) {
        this.user_id = user_id;
        this.room_id = room_id;
        this.role_id = role_id;
    }
}
