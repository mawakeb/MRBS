package nl.tudelft.sem.user.entity;

import nl.tudelft.sem.user.object.Type;
import javax.persistence.*;
import java.util.Objects;

/**
 * The User Entity.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "net_id")
    private String netId;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    /**
     * No-args constructor for Spring.
     */
    public User() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the netId
     */
    public String getNetId() {
        return netId;
    }

    /**
     * @param netId the netId
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type the type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @param hashedPassword the hashed password
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(netId, user.netId)
                && Objects.equals(name, user.name)
                && Objects.equals(hashedPassword, user.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, netId, name, hashedPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", netId='" + netId + '\'' +
                ", name='" + name + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                '}';
    }
}
