package chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "myuser")
public class MyUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean active;


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Message> messages;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "roomsowner",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> OwnerOfRooms;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "rooms_participants",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> particioants;


    public MyUser() {
    }


    public MyUser(String username, String password, boolean active/*, Set<Role> roles*/, Set<Message> messages) {
        this.username = username;
        this.password = password;
        this.active = active;
      //  this.roles = roles;
        this.messages = messages;
    }


    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }



    public List<Room> getOwnerOfRooms() {
        return OwnerOfRooms;
    }

    public void setOwnerOfRooms(List<Room> ownerOfRooms) {
        OwnerOfRooms = ownerOfRooms;
    }

    public List<Room> getParticioants() {
        return particioants;
    }

    public void setParticioants(List<Room> particioants) {
        this.particioants = particioants;
    }
}
