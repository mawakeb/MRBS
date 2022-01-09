package nl.tudelft.sem.room.repository;

import java.util.List;
import nl.tudelft.sem.room.entity.RoomNotice;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The RoomNotice Repository Interface.
 */
public interface NoticeRepository extends JpaRepository<RoomNotice, Long> {

    List<RoomNotice> findAll();

    RoomNotice findById(long id);

    List<RoomNotice> findByRoomId(long roomId);


}
