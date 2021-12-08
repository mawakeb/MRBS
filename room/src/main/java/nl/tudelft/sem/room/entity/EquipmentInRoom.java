package nl.tudelft.sem.room.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * The Equipment in Room Entity.
 */
@Entity
@Table(name = "equipmentRoom")
public class EquipmentInRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "roomId")
    private Long roomId;

    @Column(name = "equipmentId")
    private Long equipmentId;

    public EquipmentInRoom(Long roomId, Long equipmentId) {
        this.roomId = roomId;
        this.equipmentId = equipmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
}

