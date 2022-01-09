package nl.tudelft.sem.room.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The Room Entity.
 */
@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "buildingId")
    private Long buildingId;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "underMaintenance")
    private boolean underMaintenance;

    public Room() {
    }

    /**
     * Constructor for room entity.
     *
     * @param id         room id
     * @param name       name of the building
     * @param buildingId id of the building that the room is in
     * @param capacity   capacity of the room
     */
    public Room(Long id, String name, Long buildingId, int capacity) {
        this.id = id;
        this.name = name;
        this.buildingId = buildingId;
        this.capacity = capacity;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

