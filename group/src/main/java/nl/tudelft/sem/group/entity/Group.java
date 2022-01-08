package nl.tudelft.sem.group.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "secretary")
    private List<Long> secretaryIds;

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
            if (!membersIds.contains(l)) {
                membersIds.add(l);
            }
        }
    }

    /**
     * @return the id of the research group.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id of the research group.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return a list of the id-s of all the secretaries of the research group.
     */
    public List<Long> getSecretaryIds() {
        return secretaryIds;
    }

    /**
     * Sets new id-s for the secretaries of a research group.
     * @param secretaryIds the id-s of all the secretaries within that research group.
     */
    public void setSecretaryIds(List<Long> secretaryIds) {
        this.secretaryIds = secretaryIds;
    }

    /**
     * @return a list of the id-s of all the members of the research group.
     */
    public List<Long> getMembersIds() {
        return membersIds;
    }

    /**
     * Sets new id-s for the members of a research group.
     * @param membersId the id-s of all the members within that research group.
     */
    public void setMembersIds(List<Long> membersId) {
        this.membersIds = membersId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(membersIds, group.membersIds);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", membersIds=" + membersIds.toString() +
                '}';
    }
}

