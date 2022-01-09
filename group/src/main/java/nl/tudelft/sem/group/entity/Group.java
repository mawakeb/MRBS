package nl.tudelft.sem.group.entity;

import java.util.List;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * The Research group Entity.
 */
@Entity
@Table(name = "group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ElementCollection
    @Column(name = "secretaries")
    private List<Long> secretaryIds;

    @ElementCollection
    @Column(name = "members")
    private List<Long> membersIds;

    /**
     * No-args constructor for Spring.
     */
    public Group() {}

    /**
     * Constructor for the Group class.
     *
     * @param secretaryIds   ID of the secretaries of the research group.
     * @param membersIds     IDs of the members of the research group.
     */
    public Group(List<Long> secretaryIds, List<Long> membersIds) {
        this.secretaryIds = secretaryIds;
        this.membersIds = membersIds;

        // add the secretaries to the members list if they weren't already
        for (Long l : secretaryIds) {
            if (!this.membersIds.contains(l)) {
                this.membersIds.add(l);
            }
        }
    }

    /**
     * Get the id of the research group.
     *
     * @return the id of the research group.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the research group.
     *
     * @param id the id of the research group.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ids for the secretaries of a research group.
     *
     * @return a list of the id-s of all the secretaries of the research group.
     */
    public List<Long> getSecretaryIds() {
        return secretaryIds;
    }

    /**
     * Sets new ids for the secretaries of a research group.
     *
     * @param secretaryIds the ids of all the secretaries within that research group.
     */
    public void setSecretaryIds(List<Long> secretaryIds) {
        this.secretaryIds = secretaryIds;
    }

    /**
     * Gets the ids for the members of a research group.
     *
     * @return a list of the ids of all the members of the research group.
     */
    public List<Long> getMembersIds() {
        return membersIds;
    }

    /**
     * Sets new ids for the members of a research group.
     *
     * @param membersId the ids of all the members within that research group.
     */
    public void setMembersIds(List<Long> membersId) {
        this.membersIds = membersId;
    }

    /**
     * Checks if the given object is equal to this Group object.
     *
     * @param o - the object to compare against.
     * @return - true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(membersIds, group.membersIds);
    }

    /**
     * Creates a hash of this Group object.
     *
     * @return - the int hash value of that object.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * A human-friendly String representation of this Group object.
     *
     * @return - the human-readable String of this Group.
     */
    @Override
    public String toString() {
        return "Group{"
                + "id=" + id
                + ", secretaryIds=" + secretaryIds.toString()
                + ", membersIds=" + membersIds.toString()
                + '}';
    }
}

