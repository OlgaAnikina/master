package chat.repositories;

import chat.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<MyUser, String> {
    MyUser findByUsername(String username);

}
