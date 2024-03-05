package chatroom.chat.repository;

import chatroom.chat.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    UserData findByUsername(String username);
}
