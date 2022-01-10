package nl.tudelft.sem.room.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "equipmentName")
    private String equipmentName;

    /**
     * Constructor for EquipmentInRoom.
     *
     * @param roomId        id of the room that the equipment is in
     * @param equipmentName name of the equipments
     */
    public EquipmentInRoom(Long roomId, String equipmentName) {
        this.roomId = roomId;
        this.equipmentName = equipmentName;
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

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}

