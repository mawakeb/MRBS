package nl.tudelft.sem.room.repository;

import nl.tudelft.sem.room.entity.RoomNotice;
import org.h2.command.dml.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


/**
 * The RoomNotice Repository Interface.
 */
public interface NoticeRepository extends JpaRepository<RoomNotice, Long> {

    List<RoomNotice> findAll();

    RoomNotice findById(long id);

    List<RoomNotice> findByRoomId(long roomId);



}
