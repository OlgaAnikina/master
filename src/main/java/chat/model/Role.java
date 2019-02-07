package chat.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, OWNER, PARTICIPANT;


    @Override
    public String getAuthority() {
        return name();
    }
}
