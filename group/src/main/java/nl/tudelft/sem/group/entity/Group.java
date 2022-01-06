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

    @Column(name = "members")
    private List<Long> membersIds;

    /**
     * No-args constructor for Spring.
     */
    public Group() {
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

