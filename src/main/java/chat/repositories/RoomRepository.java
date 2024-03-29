package chat.repositories;

import chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomsName(String roomsName);
    Room findById(long id);
}
