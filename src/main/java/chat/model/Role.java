package chat.model;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "roles_name")
    private String rolesName;

    @Column(name = "roles_description")
    private String description;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<MyUser> users = new ArrayList<>();



    public Role() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRolesName() {
        return rolesName;
    }

    public void setRolesName(String rolesName) {
        this.rolesName = rolesName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return getRolesName();
    }

    public List<MyUser> getUsers() {
        return users;
    }

    public void setUsers(List<MyUser> users) {
        this.users = users;
    }
}
