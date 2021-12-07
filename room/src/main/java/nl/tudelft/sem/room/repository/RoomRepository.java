package nl.tudelft.sem.room.repository;


import nl.tudelft.sem.room.entity.Room;
import org.h2.command.dml.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * The Room Repository Interface.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();

    Room findById(long id);



}

