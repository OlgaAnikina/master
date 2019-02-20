package chat.services;

import chat.model.MyUser;
import chat.repositories.UserRepository;
import chat.web.rest.dto.ConvertToDTO;
import chat.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(MyUser user) {
        MyUser userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;

    }

    public List<MyUser> getFilterUsers(MyUser owner) {
        List<MyUser> resultUsers = new ArrayList<>();
        for(MyUser user:userRepository.findAll()) {
            if((!(user.getUsername().equals(owner.getUsername())))
                    &&(!(user.getUsername().equals("admin")))
                    &&(!(user.getUsername().equals("Guest")))) {
                resultUsers.add(user);
            }

        }
        return resultUsers;
    }
}
